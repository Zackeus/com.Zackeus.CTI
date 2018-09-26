package com.Zackeus.CTI.modules.agent.utils;

import com.Zackeus.CTI.modules.agent.config.AgentConfig;
import com.Zackeus.CTI.modules.agent.entity.AgentHttpEvent;
import com.Zackeus.CTI.modules.agent.entity.AgentSocketMsg;
import com.Zackeus.CTI.modules.agent.entity.Result;

/**
 * 
 * @Title:AgentStateUtil
 * @Description:TODO(坐席事件工具类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月25日 下午1:09:24
 */
public class AgentEventUtil {
	
	/**
	 * 
	 * @Title：analyzeState
	 * @Description: TODO(解析AgentGateway状态)
	 * @see：
	 * @param result
	 */
	public static Result analyzeState(Result result) {
		switch (result.getAgentState()) {
		
		case AgentConfig.AGENT_STATE_LOGIN:
			result.setAgentStateText("签入");
			result.setAgentStateColor("black");
			break;
			
		case AgentConfig.AGENT_STATE_LOGOUT:
			result.setAgentStateText("签出");
			result.setAgentStateColor("black");
			break;
			
		case AgentConfig.AGENT_STATE_FREE:
			result.setAgentStateText("示闲");
			result.setAgentStateColor("green");
			break;
			
		case AgentConfig.AGENT_STATE_BUSY:
			result.setAgentStateText("示忙");
			result.setAgentStateColor("orange");
			break;
			
		case AgentConfig.AGENT_STATE_WORK:
			result.setAgentStateText("工作");
			result.setAgentStateColor("red");
			break;
			
		case AgentConfig.AGENT_STATE_CALL:
			result.setAgentStateText("通话");
			result.setAgentStateColor("blue");
			break;

		default:
			result.setAgentStateText("未知");
			result.setAgentStateColor("black");
			break;
		}
		return result;
	}
	
	/**
	 * 
	 * @Title：getAgentSocketMsg
	 * @Description: TODO(解析事件响应体)
	 * @see：
	 * @param agentHttpEvent
	 * @return
	 */
	public static AgentSocketMsg getAgentSocketMsg(AgentHttpEvent agentHttpEvent) {
		AgentSocketMsg agentSocketMsg = null;
		switch (agentHttpEvent.getEvent().getEventType()) {
		
		case AgentConfig.AGENTSTATE_READY:
		case AgentConfig.AGENTSTATE_CANCELNOTREADY_SUCCESS:
		case AgentConfig.AGENTSTATE_CANCELWORK_SUCCESS:
		case AgentConfig.AGENTSTATE_CANCELREST_SUCCESS:
			Result readyResult = new Result();
			readyResult.setAgentStateText("示闲");
			readyResult.setAgentStateColor("green");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getMessage(), readyResult);
			break;
			
		case AgentConfig.AGENTSTATE_BUSY:
		case AgentConfig.AGENTSTATE_SETNOTREADY_SUCCESS:
		case AgentConfig.AGENTSTATE_FORCE_SETNOTREADY:
			Result busyResult = new Result();
			busyResult.setAgentStateText("示忙");
			busyResult.setAgentStateColor("orange");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getMessage(), busyResult);
			break;
			
		case AgentConfig.AGENTSTATE_WORK:
		case AgentConfig.AGENTSTATE_SETWORK_SUCCESS:
			Result workResult = new Result();
			workResult.setAgentStateText("工作");
			workResult.setAgentStateColor("red");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getMessage(), workResult);
			break;
			
		case AgentConfig.AGENTEVENT_TALKING:
			Result talkResult = new Result();
			talkResult.setAgentStateText("通话");
			talkResult.setAgentStateColor("blue");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getMessage(), talkResult);
			break;

		default:
			Result unKnownResult = new Result();
			unKnownResult.setAgentStateText("未知");
			unKnownResult.setAgentStateColor("black");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_UNKNOWN, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getMessage(), unKnownResult);
			break;
		}
		return agentSocketMsg;
	}
	
}
