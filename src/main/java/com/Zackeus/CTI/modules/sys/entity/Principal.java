package com.Zackeus.CTI.modules.sys.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.modules.agent.entity.AgentUser;

/**
 * 
 * @Title:Principal
 * @Description:TODO(授权用户信息)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月10日 上午11:46:19
 */
public class Principal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id; 				// 编号
	private String loginName; 		// 登录名
	private String name; 			// 姓名
	private boolean mobileLogin; 	// 是否手机登录
	private AgentUser agentUser;	// 坐席账号信息
	
	public Principal() {
		super();
	}
	
	public Principal(String id, String loginName, String name, boolean mobileLogin) {
		super();
		this.id = id;
		this.loginName = loginName;
		this.name = name;
		this.mobileLogin = mobileLogin;
	}

	public Principal(User user, boolean mobileLogin) {
		this.id = user.getId();
		this.loginName = user.getLoginName();
		this.name = user.getName();
		this.mobileLogin = mobileLogin;
		this.agentUser = user.getAgentUser();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMobileLogin() {
		return mobileLogin;
	}

	public void setMobileLogin(boolean mobileLogin) {
		this.mobileLogin = mobileLogin;
	}

	public AgentUser getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(AgentUser agentUser) {
		this.agentUser = agentUser;
	}

	public boolean isAdmin() {
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return StringUtils.isNotBlank(id) && StringUtils.equals(User.ADMIN_ID, id);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}