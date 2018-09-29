package com.Zackeus.CTI.modules.agent.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.Zackeus.CTI.modules.sys.entity.Principal;

/**
 * 
 * @Title:WebSocketMsg
 * @Description:TODO(坐席事件响应实体类)
 * @Company:
 * @author zhou.zhang
 * @param <T>
 * @date 2018年9月25日 下午5:15:40
 */
public class AgentSocketMsg implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer eventCode; 				// 事件类型码
	private String eventType; 				// 事件类型
	private Object content; 				// 事件内容
	private Object additionalContent; 		// 额外内容
	private Principal agentUser;			// 用户坐席

	public AgentSocketMsg() {
		super();
	}
	
	public AgentSocketMsg(Integer eventCode, String eventType, Object content) {
		super();
		this.eventCode = eventCode;
		this.eventType = eventType;
		this.content = content;
	}
	
	public AgentSocketMsg(Integer eventCode, String eventType, Object content, Object additionalContent) {
		super();
		this.eventCode = eventCode;
		this.eventType = eventType;
		this.content = content;
		this.additionalContent = additionalContent;
	}
	
	public AgentSocketMsg(Integer eventCode, String eventType, Object content, Object additionalContent,
			Principal agentUser) {
		super();
		this.eventCode = eventCode;
		this.eventType = eventType;
		this.content = content;
		this.additionalContent = additionalContent;
		this.agentUser = agentUser;
	}

	public Integer getEventCode() {
		return eventCode;
	}

	public void setEventCode(Integer eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	public Object getAdditionalContent() {
		return additionalContent;
	}

	public void setAdditionalContent(Object additionalContent) {
		this.additionalContent = additionalContent;
	}
	
	public Principal getAgentUser() {
		return agentUser;
	}

	public void setAgentUser(Principal agentUser) {
		this.agentUser = agentUser;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
