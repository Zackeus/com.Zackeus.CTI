package com.Zackeus.CTI.modules.sys.entity.agent;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

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

	private Integer eventCode; 	// 事件类型码
	private String eventType; 	// 事件类型
	private String message; 	// 消息体
	private Object object; 		// 自定义实体

	public AgentSocketMsg() {
		super();
	}
	
	public AgentSocketMsg(Integer eventCode, String eventType, String message, Object object) {
		super();
		this.eventCode = eventCode;
		this.eventType = eventType;
		this.message = message;
		this.object = object;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
	
}
