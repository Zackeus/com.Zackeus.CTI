package com.Zackeus.CTI.modules.agent.entity;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @Title:Event
 * @Description:TODO(座席事件对象)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月22日 下午2:50:20
 */
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	private String eventType; 	// 事件类型
	private String workNo; 		// 座席工号
	private Object content; 	// 事件内容对象，根据不同的事件，其相关的定义也不同

	public Event() {
		super();
	}

	public Event(String eventType, String workNo, Object content) {
		super();
		this.eventType = eventType;
		this.workNo = workNo;
		this.content = content;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
