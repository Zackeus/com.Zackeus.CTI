package com.Zackeus.CTI.modules.sys.config;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @Title:AgentConfig
 * @Description:TODO(坐席请求参数)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月20日 下午2:41:51
 */
@Configuration
public class AgentParam {
	
	@Value("${agentConfig.loginUrl}")
	private String loginUrl; 				// 坐席签入地址
	
	@Value("${agentConfig.forceLoginUrl}")
	private String forceLoginUrl; 			// 坐席强制签入地址
	
	@Autowired
	private LoginParam loginParam;
	
	public LoginParam getLoginParam() {
		return loginParam;
	}

	public void setLoginParam(LoginParam loginParam) {
		this.loginParam = loginParam;
	}

	public String getLoginUrl() {
		return loginUrl;
	}
	
	public String getForceLoginUrl() {
		return forceLoginUrl;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
