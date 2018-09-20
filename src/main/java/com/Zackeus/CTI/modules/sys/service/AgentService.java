package com.Zackeus.CTI.modules.sys.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.config.AgentConfig;
import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.service.BaseService;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.httpClient.AgentClientUtil;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.AgentEventThread;
import com.Zackeus.CTI.modules.sys.utils.AgentQueue;

/**
 * 
 * @Title:AgentService
 * @Description:TODO(坐席Service)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月20日 上午11:40:17
 */
@Service("agentService")
public class AgentService extends BaseService {

	@Autowired
	private AgentConfig agentConfig;
	
	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private AgentQueue agentQueue;

	/**
	 * 
	 * @Title：login
	 * @Description: TODO(坐席签入) 
	 * @see：
	 * @param user
	 * @throws Exception 
	 */
	public void login(User user) throws Exception {
		Map<String, Object> loginParam = new HashMap<String, Object>();
        loginParam.put("password", StringUtils.EMPTY);            			// 坐席密码(默认为空)
        loginParam.put("phonenum", user.getAgentUser().getPhonenumber());  	// 坐席电话
        loginParam.put("autoanswer", Boolean.FALSE);             			// 是否自动应答(true：自动应答 false：手动应答)
        loginParam.put("autoenteridle", Boolean.TRUE);         				// 是否自动进入空闲态(true：空闲态 false：整理态)
        loginParam.put("status", 4);                     					// 签入后的状态(4：空闲态 5：整理态)
        loginParam.put("releasephone", Boolean.TRUE);           			// 座席挂机后是否进入非长通态 (true：非长通态  false：长通态)
        loginParam.put("ip", agentConfig.getLocalIp());            			// 座席ip(默认127.0.0.1)
        HttpClientResult httpClientResult = AgentClientUtil.put(user, agentConfig.getLoginUrl() + user.getAgentUser().getWorkno(), loginParam);
        Logs.info("结果：" + httpClientResult);
	}

	/**
	 * 
	 * @Title：addQueue
	 * @Description: TODO(启动代理队列) 
	 * @see：
	 * @param user
	 */
	public void addQueue(User user) {
		AgentEventThread agentEventThread = new AgentEventThread(user);
		taskExecutor.execute(agentEventThread);
		agentQueue.eventThreadMap.put(user.getId(), agentEventThread);
	}

	/**
	 * 
	 * @Title：clearResourse
	 * @Description: TODO(资源清晰) @see：
	 * @param user
	 */
	public void clearResourse(User user) {
		if (agentQueue.eventThreadMap.containsKey(user.getId())) {
			agentQueue.eventThreadMap.get(user.getId()).end();
			agentQueue.eventThreadMap.remove(user.getId());
		}
	}
}
