package com.Zackeus.CTI.modules.agent.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.Zackeus.CTI.modules.agent.config.AgentConfig;

/**
 * 
 * @Title:AgentAlerting
 * @Description:TODO(坐席振铃实体)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月28日 上午9:16:28
 */
public class AgentAlerting implements Serializable {

	private static final long serialVersionUID = 1L;

	private String callid; 									// 呼叫流水号
	private String caller; 									// 呼叫号码
	private String type = AgentConfig.AGENT_CALL_MAIN; 		// 呼叫类型(main：去电；called：来电)

	public AgentAlerting() {
		super();
	}
	
	public AgentAlerting(String caller) {
		super();
		this.caller = caller;
	}

	public AgentAlerting(String callid, String caller, String type) {
		super();
		this.callid = callid;
		this.caller = caller;
		this.type = type;
	}

	public String getCallid() {
		return callid;
	}

	public void setCallid(String callid) {
		this.callid = callid;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
