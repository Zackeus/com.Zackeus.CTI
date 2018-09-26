package com.Zackeus.CTI.modules.agent.entity;

/**
 * 
 * @Title:AgentHttpEvent
 * @Description:TODO(坐席事件)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月22日 下午2:49:43
 */
public class AgentHttpEvent extends BasicHttpResult {

	private static final long serialVersionUID = 1L;

	private Event event; 	// 座席事件对象列表

	public AgentHttpEvent() {
		super();
	}

	public AgentHttpEvent(String message, String retcode) {
		super(message, retcode);
	}

	public AgentHttpEvent(Event event) {
		super();
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

}
