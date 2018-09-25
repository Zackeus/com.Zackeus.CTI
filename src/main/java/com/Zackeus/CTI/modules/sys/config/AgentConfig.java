package com.Zackeus.CTI.modules.sys.config;

public class AgentConfig {
	
	/*AgentGateway状态*/
	public static final int AGENT_STATE_LOGIN = 1;		// 签入状态。未使用。
	public static final int AGENT_STATE_LOGOUT = 2;		// 签出状态。对应CTT平台状态的0。
	public static final int AGENT_STATE_BUSY = 3;		// 示忙状态。对应CTI平台状态的7。
	public static final int AGENT_STATE_FREE = 4;		// 空闲状态。对应CTT平台状态的1。
	public static final int AGENT_STATE_WORK = 5;		// 工作状态。
	public static final int AGENT_STATE_CALL = 7;		// 通话态。对应CTT平台状态的2、3、4、5。
	
	/*事件类型码*/
	public static final int EVENT_UNKNOWN = -1; 		// 未知事件
	public static final int EVENT_AGENT_STATE = 1; 		// 坐席状态类事件
	public static final int EVENT_VOICE_CALL = 2; 		// 语音通话事件
	public static final int EVENT_OUTBOUND = 3; 		// 外呼相关事件
	public static final int EVENT_RECORD = 4; 			// 录放音相关
	public static final int EVENT_WORD = 5; 			// 文字交谈
	public static final int EVENT_QUALITY_TESTING = 6; 	// 实时质检事件
	public static final int EVENT_RMS = 7; 				// RMS相关事件
	public static final int EVENT_PHYSICAL_PHONE = 8; 	// 物理话机事件
	
	/*坐席事件列表*/
	/*座席状态类事件*/
	public static final String AGENTSTATE_READY = "AgentState_Ready";										// 座席示闲
	public static final String AGENTSTATE_BUSY = "AgentState_Busy";											// 座席忙事件
	public static final String AGENTSTATE_SETNOTREADY_SUCCESS = "AgentState_SetNotReady_Success";			// 示忙成功
	public static final String AGENTSTATE_CANCELNOTREADY_SUCCESS = "AgentState_CancelNotReady_Success";		// 取消示忙成功
	public static final String AGENTSTATE_FORCE_SETNOTREADY = "AgentState_Force_SetNotReady";				// 强制示忙成功
	public static final String AGENTSTATE_WORK = "AgentState_Work";											// 座席工作中
	public static final String AGENTSTATE_SETWORK_SUCCESS = "AgentState_SetWork_Success";					// 进入工作态
	public static final String AGENTSTATE_CANCELWORK_SUCCESS = "AgentState_CancelWork_Success";				// 退出工作态(进入示闲态)
	public static final String AGENTSTATE_SETREST_SUCCESS = "AgentState_SetRest_Success";					// 休息成功
	public static final String AGENTSTATE_CANCELREST_SUCCESS = "AgentState_CancelRest_Success";				// 取消休息成功

}