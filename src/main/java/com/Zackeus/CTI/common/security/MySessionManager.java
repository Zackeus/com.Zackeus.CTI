package com.Zackeus.CTI.common.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.websocket.WebSocketConfig;

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
	
	@PostConstruct
	public void init() {
		sessionManager = this;
	}
	
	/**
	 * 
	 * @Title：putSession
	 * @Description: TODO(添加session队列)
	 * @see：
	 * @param id
	 */
	public static void putSession(String id) {
		putSession(id, null);
	}
	
	public static void putSession(String id, Session session) {
		if (ObjectUtils.isEmpty(session)) {
			session = getSession();
		}
		session.setAttribute(ShiroHttpSession.DEFAULT_SESSION_ID_NAME, id);
		MySessionManager.shiroUsers.put((String) session.getAttribute(ShiroHttpSession.DEFAULT_SESSION_ID_NAME), session);
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
		return ObjectUtils.isEmpty(shiroUsers.get(id)) ? null : shiroUsers.get(id);
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
		if (ObjectUtils.isNotEmpty(session)) {
			sessionManager.sessionDAO.delete(session);
			shiroUsers.remove(id);
		}
		sessionManager.webSocketConfig.webSocketHandler().kickOutUser(id, closeStatus);
	}

}
