package com.Zackeus.CTI.modules.agent.entity;

/**
 * 
 * @Title:AgentResultStatus
 * @Description:TODO(坐席接口返回码)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月21日 下午1:12:28
 */
public class AgentHttpStatus {
	
	private Integer ajaxStatus; // 前端响应状态码
	private String agentStatus;	// 坐席接口状态吗
	private String msg;			// 类容
	
	public AgentHttpStatus() {
		super();
	}
	
	public AgentHttpStatus(Integer ajaxStatus, String agentStatus, String msg) {
		super();
		this.ajaxStatus = ajaxStatus;
		this.agentStatus = agentStatus;
		this.msg = msg;
	}
	
	public Integer getAjaxStatus() {
		return ajaxStatus;
	}

	public void setAjaxStatus(Integer ajaxStatus) {
		this.ajaxStatus = ajaxStatus;
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
