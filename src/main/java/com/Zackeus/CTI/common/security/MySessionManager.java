package com.Zackeus.CTI.common.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.websocket.WebSocketConfig;
import com.Zackeus.CTI.modules.sys.entity.User;

/**
 * 
 * @Title:SessionManager
 * @Description:TODO(会话管理类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月19日 下午1:06:35
 */
@Component
public class MySessionManager {
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private WebSocketConfig webSocketConfig;
	
	public static final Map<String, Session> shiroUsers = new ConcurrentHashMap<String, Session>();
	
	public static MySessionManager sessionManager;
	
	public static final String USER_ID = "USERID";
	public static final String AGENT_ID = "AGENTID";
	
	@PostConstruct
	public void init() {
		sessionManager = this;
	}
	
	/**
	 * 
	 * @Title：putSession
	 * @Description: TODO(注册session信息)
	 * @see：
	 * @param user
	 */
	public static void putSession(User user) {
		Session session = getSession();
		session.setAttribute(USER_ID, user.getId());
		session.setAttribute(AGENT_ID, user.getAgentUser().getWorkno());
		MySessionManager.shiroUsers.put((String) session.getAttribute(USER_ID), session);
	}
	
	/**
	 * 
	 * @Title：getSession
	 * @Description: TODO(获取当前session)
	 * @see：
	 * @return
	 */
	public static Session getSession(){
		try {
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (ObjectUtils.isEmpty(session)){
				session = subject.getSession();
			} else {
				return session;
			}
		} catch (InvalidSessionException e){
			Logs.error("获取session异常：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 
	 * @Title：getSession
	 * @Description: TODO(获取指定用户session)
	 * @see：
	 * @param id
	 * @return
	 */
	public static Session getSession(String id) {
		return shiroUsers.containsKey(id) ? shiroUsers.get(id) : null;
	}
	
	/**
	 * 
	 * @Title：deleteSession
	 * @Description: TODO(剔除用户Session)
	 * @see：
	 * @param session
	 */
	public static void deleteSession(String id, CloseStatus closeStatus) {
		Session session = getSession(id);
		if (shiroUsers.containsKey(id)) {
			sessionManager.sessionDAO.delete(session);
			shiroUsers.remove(id);
		}
		sessionManager.webSocketConfig.webSocketHandler().kickOutUser(id, closeStatus);
	}

}
