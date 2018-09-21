package com.Zackeus.CTI.modules.sys.utils;

import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.SpringContextUtil;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.service.AgentService;

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
	
	public AgentEventThread(User user) {
        super();
        this.user = user;
        this.agentService = SpringContextUtil.getBeanByName(AgentService.class);
	}

	@Override
	public void run() {
		while (isAlive) {
			try {
				HttpClientResult httpClientResult = agentService.event(user);
				Logs.info(httpClientResult);
				if (StringUtils.isNotBlank(httpClientResult.getContent())) {
					Logs.info("事件：" + httpClientResult.getContent());
				} else {
					Thread.sleep(500);
				}
			} catch (Exception e) {
				Logs.error("在线代理事件获取异常：" + e.getMessage());
			}
		}
	}
	
    public void end() {
        isAlive = Boolean.FALSE;
    }
}
