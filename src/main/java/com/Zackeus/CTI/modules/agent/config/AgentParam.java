package com.Zackeus.CTI.modules.agent.config;

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
	
	@Value("${agentConfig.logoutUrl}")
	private String logoutUrl; 				// 坐席签出地址
	
	@Value("${agentConfig.agenteventUrl}")
	private String agenteventUrl; 			// 坐席事件地址
	
	@Value("${agentConfig.agentStatusUrl}")
	private String agentStatusUrl; 			// 当前座席的状态地址
	
	@Value("${agentConfig.resetSkillUrl}")
	private String resetSkillUrl; 			// 重置技能地址
	
	@Value("${agentConfig.sayFreeUrl}")
	private String sayFreeUrl; 				// 座席示闲地址
	
	@Value("${agentConfig.sayBusyUrl}")
	private String sayBusyUrl; 				// 坐席示忙地址
	
	@Value("${agentConfig.workUrl}")
	private String workUrl; 				// 进入工作地址
	
	@Value("${agentConfig.cancelWorkUrl}")
	private String cancelWorkUrl; 			// 退出工作地址
	
	@Value("${agentConfig.callOutUrl}")
	private String callOutUrl;				// 外呼地址
	
	@Value("${agentConfig.phonePickUpUrl}")
	private String phonePickUpUrl;			// 话机联动应答地址
	
	@Value("${agentConfig.phoneHangUpUrl}")
	private String phoneHangUpUrl;			// 话机联动拒接地址
	
	@Value("${agentConfig.answerUrl}")
	private String answerUrl;				// 应答地址
	
	@Value("${agentConfig.releaseUrl}")
	private String releaseUrl;				// 挂断呼叫地址
	
	@Autowired
	private LoginParam loginParam;			// 登录参数
	
	@Autowired
	private CallParam callParam;			// 呼叫参数
	
	public LoginParam getLoginParam() {
		return loginParam;
	}
	
	public void setLoginParam(LoginParam loginParam) {
		this.loginParam = loginParam;
	}
	
	public CallParam getCallParam() {
		return callParam;
	}

	public void setCallParam(CallParam callParam) {
		this.callParam = callParam;
	}

	public String getLoginUrl() {
		return loginUrl;
	}
	
	public String getForceLoginUrl() {
		return forceLoginUrl;
	}
	
	public String getLogoutUrl() {
		return logoutUrl;
	}

	public String getAgenteventUrl() {
		return agenteventUrl;
	}
	
	public String getResetSkillUrl() {
		return resetSkillUrl;
	}
	
	public String getAgentStatusUrl() {
		return agentStatusUrl;
	}
	
	public String getSayFreeUrl() {
		return sayFreeUrl;
	}

	public String getSayBusyUrl() {
		return sayBusyUrl;
	}

	public String getWorkUrl() {
		return workUrl;
	}

	public String getCancelWorkUrl() {
		return cancelWorkUrl;
	}
	
	public String getCallOutUrl() {
		return callOutUrl;
	}
	
	public String getPhonePickUpUrl() {
		return phonePickUpUrl;
	}
	
	public String getPhoneHangUpUrl() {
		return phoneHangUpUrl;
	}

	public String getAnswerUrl() {
		return answerUrl;
	}

	public String getReleaseUrl() {
		return releaseUrl;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
