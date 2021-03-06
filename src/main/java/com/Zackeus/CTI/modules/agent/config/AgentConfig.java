package com.Zackeus.CTI.modules.agent.config;

public class AgentConfig {
	
	/*接口参数*/
	public static String AGENT_HTTP_USERID = "userId";			// 用户编号
	public static String AGENT_HTTP_LOGINNAME = "loginName";	// 用户登录名
	public static String AGENT_HTTP_POSTURL = "postUrl";		// 事件推送地址
	
	/*呼叫类型*/
	public static String AGENT_CALL_MAIN = "main";		// 去电
	public static String AGENT_CALL_CALLED = "called";	// 来电
	
	/*录音控制标识*/
	public static final String AGENT_RECORD_PLAY = "play"; 					// 回放
	public static final String AGENT_RECORD_STOP_PLAY = "stopplay"; 		// 停止
	public static final String AGENT_RECORD_PAUSE_RECORD = "pauserecord"; 	// 暂停
	public static final String AGENT_RECORD_RESUME_RECORD = "resumerecord"; // 恢复
	public static final String AGENT_RECORD_FORE_FAST = "forefast"; 		// 快进
	public static final String AGENT_RECORD_BACK_FAST = "backfast"; 		// 快退
	
	/*代理数据*/
	public static String AGENT_DATA_COOKIE = "cookie";			// cookie
	public static String AGENT_DATA_CALLID = "CALL_ID";			// 呼叫流水号
	public static String AGENT_DATA_CALLNUM = "CALL_NUM";		// 呼叫号码
	public static String AGENT_DATA_POSTURL = "POST_URL";		// 事件推送地址
	public static String AGENT_DATA_EVENTDATE = "EVENT_DATE";	// 最新事件日期
	public static String AGENT_DATA_ISHTTP = "IS_HTTP";			// 是否为接口登录
	public static String AGENT_DATA_RECORD = "RECORD";			// 录音数据
	
	
	/*AgentGateway状态*/
	public static final int AGENT_STATE_LOGIN = 1;		// 签入状态。未使用。
	public static final int AGENT_STATE_LOGOUT = 2;		// 签出状态。对应CTT平台状态的0。
	public static final int AGENT_STATE_BUSY = 3;		// 示忙状态。对应CTI平台状态的7。
	public static final int AGENT_STATE_FREE = 4;		// 空闲状态。对应CTT平台状态的1。
	public static final int AGENT_STATE_WORK = 5;		// 工作状态。
	public static final int AGENT_STATE_CALL = 7;		// 通话态。对应CTT平台状态的2、3、4、5。
	
	/*事件类型码*/
	public static final int EVENT_UNDEFINED = -1; 		// 未定义事件
	public static final int EVENT_AGENT_STATE = 1; 		// 坐席状态事件
	public static final int EVENT_VOICE_CALL = 2; 		// 呼叫请求事件
	public static final int EVENT_VOICE_RING = 3; 		// 坐席来电事件
	public static final int EVENT_VOICE_END = 4; 		// 语音通话结束事件
	public static final int EVENT_RECORD_START = 5; 	// 录音开始事件
	public static final int EVENT_RECORD_END = 6; 		// 录音束事件
	public static final int EVENT_RECORD_PLAY = 7; 		// 录音播放开始事件
	public static final int EVENT_RECORD_PLAY_SUCC = 8; // 录音播放成功事件
	public static final int EVENT_RECORD_PLAY_FAIL = 9; // 录音播放失败事件
	public static final int EVENT_RECORD_DONE = 10;  	// 录音播放停止事件
	
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
	
	/*语音通话事件*/
	public static final String AGENTEVENT_TALKING = "AgentEvent_Talking";									// 座席进入Talking
	public static final String AGENTEVENT_CUSTOMER_ALERTING = "AgentEvent_Customer_Alerting";				// 对方振铃
	public static final String AGENTEVENT_CALL_RELEASE = "AgentEvent_Call_Release";                       	// 座席退出呼叫
	public static final String AGENTEVENT_CUSTOMER_RELEASE = "AgentEvent_Customer_Release";                 // 客户退出呼叫
	public static final String AGENTEVENT_CALL_OUT_FAIL = "AgentEvent_Call_Out_Fail";						// 外呼失败
	public static final String AGENTEVENT_INSIDE_CALL_FAIL = "AgentEvent_Inside_Call_Fail";					// 内部呼叫失败
	public static final String AGENTEVENT_NO_ANSWER = "AgentEvent_No_Answer";								// 座席久不应答
	
	/*物理话机事件*/
	public static final String AGENTOTHER_PHONEALERTING = "AgentOther_PhoneAlerting";                       // 座席物理话机振铃
	public static final String AGENTOTHER_PHONEOFFHOOK = "AgentOther_PhoneOffhook";                       	// 座席物理话机摘机
	public static final String AGENTOTHER_PHONERELEASE = "AgentOther_PhoneRelease";                       	// 座席物理话机挂机
	
	/*录放音相关*/
	public static final String AGENTMEDIAEVENT_RECORD = "AgentMediaEvent_Record";                       	// 录音开始
	public static final String AGENTMEDIAEVENT_STOPRECORDDONE = "AgentMediaEvent_StopRecordDone"; 			// 停止录音成功
	public static final String AGENTMEDIAEVENT_PLAY = "AgentMediaEvent_Play";								// 录音播放开始
	public static final String AGENTMEDIAEVENT_PLAY_SUCC = "AgentMediaEvent_Play_Succ";						// 录音播放成功
	public static final String AGENTMEDIAEVENT_PLAY_FAIL = "AgentMediaEvent_Play_Fail";						// 录音播放失败
	public static final String AGENTMEDIAEVENT_STOPPLAYDONE = "AgentMediaEvent_StopPlayDone";				// 录音播放停止
	
}
