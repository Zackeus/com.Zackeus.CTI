package com.Zackeus.CTI.common.config;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @Title:AgentConfig
 * @Description:TODO(坐席信息配置类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月20日 下午2:41:51
 */
@Component
public class AgentConfig {
	
	@Value("${agentConfig.localIp}")
	private String localIp;		// 默认坐席IP
	
	@Value("${agentConfig.loginUrl}")
	private String loginUrl; 	// 坐席签入地址

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	public String getLocalIp() {
		return localIp;
	}

	public void setLocalIp(String localIp) {
		this.localIp = localIp;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
