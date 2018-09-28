package com.Zackeus.CTI.modules.agent.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.Zackeus.CTI.common.utils.JsonMapper;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.SpringContextUtil;
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
	private boolean isAlive = Boolean.TRUE;
	
	// 代理数据(呼叫流水号，呼叫号码)
	private Map<String, Object> agentEventMap;
	
	public AgentEventThread(User user) {
        super();
        this.user = user;
        this.agentService = SpringContextUtil.getBeanByName(AgentService.class);
        this.agentEventMap = new ConcurrentHashMap<String, Object>();
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
	
	public void setAgentEventData(String key, Object object) {
		agentEventMap.put(key, object);
	}
	
	public Object getAgentEventData(String key) {
		return agentEventMap.get(key);
	}
	
	public void clearAgentEventData() {
		agentEventMap.clear();
	}

	public void end() {
        isAlive = Boolean.FALSE;
    }
}
