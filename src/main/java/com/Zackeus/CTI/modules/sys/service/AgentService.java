package com.Zackeus.CTI.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.service.BaseService;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.modules.sys.config.AgentParam;
import com.Zackeus.CTI.modules.sys.config.LoginParam;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.entity.agent.AgentHttpResult;
import com.Zackeus.CTI.modules.sys.utils.AgentClientUtil;
import com.Zackeus.CTI.modules.sys.utils.AgentEventThread;
import com.Zackeus.CTI.modules.sys.utils.AgentQueue;
import com.alibaba.fastjson.JSON;

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
	private TaskExecutor taskExecutor;

	@Autowired
	private AgentParam defaultAgentParam;
	
	@Autowired
	private AgentQueue agentQueue;
	
	private static String AGENT_ID = "{agentid}";
	
	/**
	 * 
	 * @Title：login
	 * @Description: TODO(坐席签入)
	 * @see：
	 * @param user
	 * @throws Exception
	 */
	public void login(User user) throws Exception {
		login(user, null);
	}
	
	public void login(User user, LoginParam loginParam) throws Exception {
		if (ObjectUtils.isEmpty(loginParam)) {
			loginParam = defaultAgentParam.getLoginParam();
		}
		loginParam.setPhonenum(user.getAgentUser().getPhonenumber());
		AgentHttpResult loginResult = JSON.parseObject(AgentClientUtil.put(user, defaultAgentParam.getLoginUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()), loginParam).getContent(), AgentHttpResult.class);
		if (StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), loginResult.getRetcode())) {
			// 登录成功
			return;
		}
		if (StringUtils.equals(HttpStatus.AS_HAS_LOGIN.getAgentStatus(), loginResult.getRetcode())) {
			// 坐席已登录进行强制登录
			AgentHttpResult forceLoginResult = JSON.parseObject(AgentClientUtil.put(user, defaultAgentParam.getForceLoginUrl().
					replace(AGENT_ID, user.getAgentUser().getWorkno()), loginParam).getContent(), AgentHttpResult.class);
			if (StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), forceLoginResult.getRetcode())) {
				return;
			}
			throw new MyException(HttpStatus.AS_ERROR.getAjaxStatus(), forceLoginResult.getMessage());
		}
		throw new MyException(HttpStatus.AS_ERROR.getAjaxStatus(), loginResult.getMessage());
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
