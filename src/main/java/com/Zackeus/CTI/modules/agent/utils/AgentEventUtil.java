package com.Zackeus.CTI.modules.agent.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.modules.agent.config.AgentConfig;
import com.Zackeus.CTI.modules.agent.entity.AgentAlerting;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;
import com.Zackeus.CTI.modules.agent.entity.AgentHttpEvent;
import com.Zackeus.CTI.modules.agent.entity.AgentRecord;
import com.Zackeus.CTI.modules.agent.entity.AgentSocketMsg;
import com.Zackeus.CTI.modules.agent.entity.Result;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.sys.entity.Principal;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.alibaba.fastjson.JSON;

/**
 * 
 * @Title:AgentStateUtil
 * @Description:TODO(坐席事件工具类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月25日 下午1:09:24
 */
@Component
public class AgentEventUtil {
	
	@Autowired
	private AgentService agentService;
	
	/**
	 * 
	 * @Title：analyzeState
	 * @Description: TODO(解析AgentGateway状态)
	 * @see：
	 * @param result
	 */
	public Result analyzeState(Result result) {
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
	public AgentSocketMsg getAgentSocketMsg(AgentHttpEvent agentHttpEvent, User user) {
		AgentSocketMsg agentSocketMsg = null;
		switch (agentHttpEvent.getEvent().getEventType()) {
		/*坐席状态类事件*/
		case AgentConfig.AGENTSTATE_READY:
		case AgentConfig.AGENTSTATE_CANCELNOTREADY_SUCCESS:
		case AgentConfig.AGENTSTATE_CANCELWORK_SUCCESS:
		case AgentConfig.AGENTSTATE_CANCELREST_SUCCESS:
			Result readyResult = new Result();
			readyResult.setAgentState(AgentConfig.AGENT_STATE_FREE);
			readyResult.setAgentStateText("示闲");
			readyResult.setAgentStateColor("green");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent(), readyResult);
			break;
			
		case AgentConfig.AGENTSTATE_BUSY:
		case AgentConfig.AGENTSTATE_SETNOTREADY_SUCCESS:
		case AgentConfig.AGENTSTATE_FORCE_SETNOTREADY:
			Result busyResult = new Result();
			busyResult.setAgentState(AgentConfig.AGENT_STATE_BUSY);
			busyResult.setAgentStateText("示忙");
			busyResult.setAgentStateColor("orange");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent(), busyResult);
			break;
			
		case AgentConfig.AGENTSTATE_WORK:
		case AgentConfig.AGENTSTATE_SETWORK_SUCCESS:
			Result workResult = new Result();
			workResult.setAgentState(AgentConfig.AGENT_STATE_WORK);
			workResult.setAgentStateText("工作");
			workResult.setAgentStateColor("red");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent(), workResult);
			break;
			
		case AgentConfig.AGENTEVENT_TALKING:
			Result talkResult = new Result();
			talkResult.setAgentState(AgentConfig.AGENT_STATE_CALL);
			talkResult.setAgentStateText("通话");
			talkResult.setAgentStateColor("blue");
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_AGENT_STATE, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent(), talkResult);
			break;
			
		/*座机振铃事件*/
		case AgentConfig.AGENTOTHER_PHONEALERTING:
			// 事件消息为空 表明为主动呼叫事件 否则为坐席来电事件
			if (ObjectUtils.isEmpty(agentHttpEvent.getEvent().getContent())) {
				/*坐席去电事件*/
				agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_VOICE_CALL, agentHttpEvent.getEvent().getEventType(), 
						new AgentAlerting((String) agentService.getAgentEventData(user, AgentConfig.AGENT_DATA_CALLNUM)));
			} else {
				/*坐席来电事件*/
				AgentAlerting agentAlerting = JSON.parseObject(agentHttpEvent.getEvent().getContent().toString(), AgentAlerting.class);
				// 标注呼叫类型为来电
				agentAlerting.setType(AgentConfig.AGENT_CALL_CALLED);
				agentService.insertCallData(user, new AgentCallData(agentAlerting));
				agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_VOICE_RING, agentHttpEvent.getEvent().getEventType(), agentAlerting);
			}
			break;
			
		/*语音通话结束*/
		case AgentConfig.AGENTEVENT_CALL_OUT_FAIL:
		case AgentConfig.AGENTEVENT_INSIDE_CALL_FAIL:
		case AgentConfig.AGENTEVENT_CALL_RELEASE:
		case AgentConfig.AGENTEVENT_NO_ANSWER:
		case AgentConfig.AGENTOTHER_PHONERELEASE:
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_VOICE_END, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent());
			break;
			
		/*对方振铃 插入记录*/
		case AgentConfig.AGENTEVENT_CUSTOMER_ALERTING:
			agentService.insertCallData(user, JSON.parseObject(agentHttpEvent.getEvent().getContent().toString(), AgentCallData.class));
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_UNDEFINED, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent());
			break;
			
		/*录音开始 插入记录*/
		case AgentConfig.AGENTMEDIAEVENT_RECORD:
			agentService.insertRecord(user, JSON.parseObject(agentHttpEvent.getEvent().getContent().toString(), AgentRecord.class));
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_RECORD_START, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent());
			break;
			
		/*录音结束 更新记录*/
		case AgentConfig.AGENTMEDIAEVENT_STOPRECORDDONE:
			AgentRecord agentRecord = agentService.updateRecordEndDate(user);
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_RECORD_END, agentHttpEvent.getEvent().getEventType(), 
					agentRecord);
			break;
			
		/*未定义事件*/
		default:
			agentSocketMsg = new AgentSocketMsg(AgentConfig.EVENT_UNDEFINED, agentHttpEvent.getEvent().getEventType(), 
					agentHttpEvent.getEvent().getContent());
			break;
		}
		agentSocketMsg.setUser(new Principal(user, Boolean.FALSE));
		return agentSocketMsg;
	}
	
}
