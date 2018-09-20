package com.Zackeus.CTI.modules.sys.utils;

import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.modules.sys.entity.User;

/**
 * 
 * @Title:AgentEventThread
 * @Description:TODO(在线代理事件)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月20日 上午11:29:57
 */
public class AgentEventThread implements Runnable {
	
	private User user;
	private boolean isAlive = Boolean.TRUE;
	
	public AgentEventThread(User user) {
        super();
        this.user = user;
	}

	@Override
	public void run() {
		while (isAlive) {
			Logs.info(user.getName());
			UserUtils.sendMessageToUser(user, "事件代理：" + user.getName());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Logs.error("在线代理事件获取异常：" + e.getMessage());
			}
		}
	}
	
    public void end() {
        isAlive = Boolean.FALSE;
    }
}
