package com.Zackeus.CTI.modules.agent.utils;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.Zackeus.CTI.common.utils.DateUtils;
import com.Zackeus.CTI.common.utils.JsonMapper;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.SpringContextUtil;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.httpClient.HttpClientUtil;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.modules.agent.config.AgentConfig;
import com.Zackeus.CTI.modules.agent.entity.AgentHttpEvent;
import com.Zackeus.CTI.modules.agent.entity.AgentSocketMsg;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;
import com.google.common.collect.ImmutableMap;

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
	private AgentEventUtil agentEventUtil;
	private User user;
	private boolean isAlive = Boolean.TRUE;
	
	// 代理数据(请求cookie， 呼叫流水号，呼叫号码，推送地址)
	private Map<String, Object> agentEventMap;
	
	/**
	 * 
	 * @param user 用户实体
	 * @param cookie 请求cookie
	 * @param postUrl 推送地址
	 * @param isHttp 是否为接口登录
	 */
	public AgentEventThread(User user, String cookie, String postUrl, Boolean isHttp) {
        super();
        this.user = user;
        this.agentService = SpringContextUtil.getBeanByName(AgentService.class);
        this.agentEventUtil = SpringContextUtil.getBeanByName(AgentEventUtil.class);
        this.agentEventMap = new ConcurrentHashMap<String, Object>();
        setAgentEventData(AgentConfig.AGENT_DATA_COOKIE, cookie);
        setAgentEventData(AgentConfig.AGENT_DATA_POSTURL, postUrl);
        setAgentEventData(AgentConfig.AGENT_DATA_EVENTDATE, new Date());
        setAgentEventData(AgentConfig.AGENT_DATA_ISHTTP, isHttp);
	}

	@Override
	public void run() {
		while (isAlive) {
			try {
				// 接口登录 超过一小时未操作登出用户
				if ((Boolean) getAgentEventData(AgentConfig.AGENT_DATA_ISHTTP) 
						&& DateUtils.pastHour((Date) getAgentEventData(AgentConfig.AGENT_DATA_EVENTDATE)) >= 1) {
					UserUtils.kickOutUser(user, HttpStatus.SC_SESSION_EXPRIES);
				}
				
				AgentHttpEvent agentHttpEvent = agentService.event(user);
				if (ObjectUtils.isNotEmpty(agentHttpEvent.getEvent())) {
					// 事件触发
					// 接口登录更新事件日期
					if ((Boolean) getAgentEventData(AgentConfig.AGENT_DATA_ISHTTP)) {
						setAgentEventData(AgentConfig.AGENT_DATA_EVENTDATE, new Date());
					}
					AgentSocketMsg agentSocketMsg = agentEventUtil.getAgentSocketMsg(agentHttpEvent, user);
					//推送地址不为空 则进行事件信息推送
					if (StringUtils.isNotBlank((String) getAgentEventData(AgentConfig.AGENT_DATA_POSTURL))) {
						HttpClientUtil.doPostJson((String) getAgentEventData(AgentConfig.AGENT_DATA_POSTURL), 
								ImmutableMap.<String, String>builder().put(AgentConfig.AGENT_DATA_COOKIE, 
										(String) getAgentEventData(AgentConfig.AGENT_DATA_COOKIE)).build(), agentSocketMsg);
					}
					UserUtils.sendMessageToUser(user, JsonMapper.toJsonString(agentSocketMsg));
				} else {
					Thread.sleep(500);
				}
			} catch (Exception e) {
				Logs.error("在线代理事件获取异常：" + e.getMessage());
			}
		}
	}
	
	public User getUser() {
		return user;
	}

	public void setAgentEventData(String key, Object object) {
		agentEventMap.put(key, object);
	}
	
	public Object getAgentEventData(String key) {
		return agentEventMap.get(key);
	}
	
	public void clearAgentEventData() {
		agentEventMap.remove(AgentConfig.AGENT_DATA_CALLID);
		agentEventMap.remove(AgentConfig.AGENT_DATA_CALLNUM);
		agentEventMap.remove(AgentConfig.AGENT_DATA_RECORD);
	}

	public void end() {
        isAlive = Boolean.FALSE;
    }
}
