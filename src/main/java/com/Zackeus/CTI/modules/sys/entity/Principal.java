package com.Zackeus.CTI.modules.sys.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.Zackeus.CTI.common.utils.StringUtils;

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
	
	public static final String ADMIN_ID = "1";
	
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
	}

	public String getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getName() {
		return name;
	}

	public boolean isMobileLogin() {
		return mobileLogin;
	}
	
	public boolean isAdmin() {
		return isAdmin(this.id);
	}
	
	public static boolean isAdmin(String id){
		return StringUtils.isNotBlank(id) && StringUtils.equals(ADMIN_ID, id);
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}