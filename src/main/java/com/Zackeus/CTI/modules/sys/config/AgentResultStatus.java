package com.Zackeus.CTI.modules.sys.config;

/**
 * 
 * @Title:AgentResultStatus
 * @Description:TODO(结果码)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月21日 下午1:12:28
 */
public class AgentResultStatus {
	
	private Integer ajaxStatus; // 前端响应状态码
	private String agentStatus;	// 坐席接口状态吗
	private String msg;			// 类容
	
	public AgentResultStatus() {
		super();
	}
	
	public AgentResultStatus(Integer ajaxStatus, String agentStatus, String msg) {
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

	public static final AgentResultStatus AS_SUCCESS = new AgentResultStatus(300000, "0", "成功");
	public static final AgentResultStatus AS_GET_EVENT_ERROR = new AgentResultStatus(300001, "000-001", "获取Agent事件的方法错误");
	public static final AgentResultStatus AS_NO_AUTHORITY = new AgentResultStatus(300003, "000-003", "没有权限调用接口");
	public static final AgentResultStatus AS_LOGIN_INVALID_PARAMTER = new AgentResultStatus(310001, "100-001", "签入参数为空或者不合法");
	public static final AgentResultStatus AS_HAS_LOGIN = new AgentResultStatus(310002, "100-002", "座席已经登录");

}
