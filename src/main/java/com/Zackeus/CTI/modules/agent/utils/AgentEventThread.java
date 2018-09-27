package com.Zackeus.CTI.modules.agent.utils;

import com.Zackeus.CTI.common.utils.JsonMapper;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.SpringContextUtil;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.modules.agent.entity.AgentHttpEvent;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:AgentEventThread
 * @Description:TODO(在线代理事件)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月20日 上午11:29:57
 */
public class AgentEventThread implements Runnable {
	
	private AgentService agentService;
	private User user;
	private String callId = StringUtils.EMPTY;
	private String called = StringUtils.EMPTY;
	private boolean isAlive = Boolean.TRUE;
	
	public AgentEventThread(User user) {
        super();
        this.user = user;
        this.agentService = SpringContextUtil.getBeanByName(AgentService.class);
	}

	@Override
	public void run() {
		while (isAlive) {
			try {
				AgentHttpEvent agentHttpEvent = agentService.event(user);
				if (ObjectUtils.isNotEmpty(agentHttpEvent.getEvent())) {
					Logs.info("触发事件：" + agentHttpEvent.getEvent().getEventType() + " ; " + agentHttpEvent.getEvent().getContent());
					UserUtils.sendMessageToUser(user, JsonMapper.toJsonString(AgentEventUtil.getAgentSocketMsg(agentHttpEvent, user)));
				} else {
					Thread.sleep(500);
				}
			} catch (Exception e) {
				Logs.error("在线代理事件获取异常：" + e.getMessage());
			}
		}
	}
	
    public String getCallId() {
		return callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public String getCalled() {
		return called;
	}

	public void setCalled(String called) {
		this.called = called;
	}

	public void end() {
        isAlive = Boolean.FALSE;
    }
}
