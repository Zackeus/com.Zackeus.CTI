package com.Zackeus.CTI.common.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.springframework.web.socket.CloseStatus;

import com.Zackeus.CTI.common.utils.HttpStatus;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:SessionListener
 * @Description:TODO(Shiro session监听)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月11日 下午4:47:26
 */
public class SessionListener implements org.apache.shiro.session.SessionListener {

	/**
	 * 会话创建触发 已进入shiro的过滤连就触发这个方法
	 */
	@Override
	public void onStart(Session session) {
		Logs.info(session.getId() + ": 会话创建");
	}

	/**
	 * 退出会话
	 */
	@Override
	public void onStop(Session session) {
		Logs.info(session.getId() + ": 退出会话");
		UserUtils.kickOutUser(new User((String) session.getAttribute(ShiroHttpSession.DEFAULT_SESSION_ID_NAME)), CloseStatus.NORMAL);
	}

	/**
	 * 会话过期时触发
	 */
	@Override
	public void onExpiration(Session session) {
		Logs.info(session.getId() + ": 会话过期");
		UserUtils.kickOutUser(new User((String) session.getAttribute(ShiroHttpSession.DEFAULT_SESSION_ID_NAME)), HttpStatus.SC_SESSION_EXPRIES);
	}
}