package com.Zackeus.CTI.common.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
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
public class MySessionManager extends DefaultWebSessionManager {
	
	@Autowired
	public SessionDAO sessionDAO;
	
	@Autowired
	public WebSocketConfig webSocketConfig;
	
	public final Map<String, Session> shiroUsers = new ConcurrentHashMap<String, Session>();
	
	public static final String USER_ID = "USERID";
	public static final String AGENT_ID = "AGENTID";
	
	/**
	 * 
	 * @Title：putSession
	 * @Description: TODO(注册session信息)
	 * @see：
	 * @param user
	 */
	public void putSession(User user) {
		Session session = getSession();
		session.setAttribute(USER_ID, user.getId());
		session.setAttribute(AGENT_ID, user.getAgentUser().getWorkno());
		shiroUsers.put((String) session.getAttribute(USER_ID), session);
	}
	
	/**
	 * 
	 * @Title：getSession
	 * @Description: TODO(获取当前session)
	 * @see：
	 * @return
	 */
	public Session getSession(){
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
	public Session getSession(String id) {
		return shiroUsers.containsKey(id) ? shiroUsers.get(id) : null;
	}
	
	/**
	 * 
	 * @Title：deleteSession
	 * @Description: TODO(剔除用户Session)
	 * @see：
	 * @param session
	 */
	public void deleteSession(String id, CloseStatus closeStatus) {
		Session session = getSession(id);
		if (shiroUsers.containsKey(id)) {
			sessionDAO.delete(session);
			shiroUsers.remove(id);
		}
		webSocketConfig.webSocketHandler().kickOutUser(id, closeStatus);
	}

}
