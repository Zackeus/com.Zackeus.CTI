package com.Zackeus.CTI.modules.agent.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.common.utils.AssertUtil;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.modules.agent.config.AgentConfig;
import com.Zackeus.CTI.modules.agent.config.AgentParam;
import com.Zackeus.CTI.modules.agent.config.CallParam;
import com.Zackeus.CTI.modules.agent.config.LoginParam;
import com.Zackeus.CTI.modules.agent.dao.AgentDao;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;
import com.Zackeus.CTI.modules.agent.entity.AgentHttpEvent;
import com.Zackeus.CTI.modules.agent.entity.AgentHttpResult;
import com.Zackeus.CTI.modules.agent.entity.AgentRecord;
import com.Zackeus.CTI.modules.agent.entity.Result;
import com.Zackeus.CTI.modules.agent.utils.AgentClientUtil;
import com.Zackeus.CTI.modules.agent.utils.AgentEventThread;
import com.Zackeus.CTI.modules.agent.utils.AgentQueue;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 
 * @Title:AgentService
 * @Description:TODO(坐席Service)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月20日 上午11:40:17
 */
@Service("agentService")
public class AgentService extends CrudService<AgentDao, AgentCallData> {
	
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
		AgentHttpResult<Result> loginResult = JSON.parseObject(AgentClientUtil.put(user, defaultAgentParam.getLoginUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()), loginParam).getContent(), new TypeReference<AgentHttpResult<Result>>(){});
		// 登录成功
		if (StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), loginResult.getRetcode())) {
			resetskill(user);
			return;
		}
		// 坐席已登录进行强制登录
		if (StringUtils.equals(HttpStatus.AS_HAS_LOGIN.getAgentStatus(), loginResult.getRetcode())) {
			forceLogin(user, loginParam);
			resetskill(user);
			return;
		}
		throw new MyException(HttpStatus.AS_ERROR.getAjaxStatus(), loginResult.getMessage());
	}
	
	/**
	 * 
	 * @Title：forceLogin
	 * @Description: TODO(强制签入)
	 * @see：
	 * @param user
	 * @param loginParam
	 * @throws Exception 
	 */
	public void forceLogin(User user, LoginParam loginParam) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.put(user, defaultAgentParam.getForceLoginUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()), loginParam);
		assertAgent(httpClientResult, "坐席强制签入请求失败", Result.class);
	}
	
	/**
	 * 
	 * @Title：resetskill
	 * @Description: TODO(重置技能队列)
	 * @see：
	 * @param user
	 * @throws Exception
	 */
	public void resetskill(User user) throws Exception {
		try {
			HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getResetSkillUrl().
					replace(AGENT_ID, user.getAgentUser().getWorkno()), null);
			assertAgent(httpClientResult, "坐席重置技能队列请求失败", Result.class);
		} catch (Exception e) {
			// 踢出同一账号
			/*注：重置技能队列是坐席签入成功后执行，因此此方法失败但须踢出先前在线的同一账号*/
			UserUtils.kickOutUser(user,  HttpStatus.SC_KICK_OUT);
			throw e;
		}
	}
	
	/**
	 * 
	 * @Title：event
	 * @Description: TODO(坐席事件)
	 * @see：
	 * @param user
	 * @throws Exception
	 */
	public AgentHttpEvent event(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.get(user, defaultAgentParam.getAgenteventUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()));
		AssertUtil.isTrue(HttpStatus.SC_OK == httpClientResult.getCode(), HttpStatus.AS_ERROR.getAjaxStatus(),
				"坐席事件请求失败：" + httpClientResult.getCode());
		AgentHttpEvent agentHttpEvent = JSON.parseObject(httpClientResult.getContent(), AgentHttpEvent.class);
		AssertUtil.isTrue(StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), agentHttpEvent.getRetcode()), 
				HttpStatus.AS_ERROR.getAjaxStatus(), agentHttpEvent.getMessage());
		return agentHttpEvent;
	}
	
	/**
	 * 
	 * @Title：agentStatus
	 * @Description: TODO(获取座席当前的状态)
	 * @see：
	 * @param user
	 * @return 
	 * @throws Exception 
	 */
	public AgentHttpResult<Result> getAgentState(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.get(user, defaultAgentParam.getAgentStatusUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()));
		return assertAgent(httpClientResult, "当前座席状态请求失败", Result.class);
	}
	
	/**
	 * 
	 * @return 
	 * @throws Exception 
	 * @Title：ready
	 * @Description: TODO(座席示闲)
	 * @see：
	 */
	public AgentHttpResult<Result> ready(User user) throws Exception {
		// 当前为工作状态时退出工作态
		if (AgentConfig.AGENT_STATE_WORK == getAgentState(user).getResult().getAgentState()) {
			outwork(user);
			return ready(user);
		}
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getSayFreeUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "进入空闲态请求失败", Result.class);
	}
	
	/**
	 * 
	 * @Title：busy
	 * @Description: TODO(坐席示忙)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public AgentHttpResult<Result> busy(User user) throws Exception {
		// 当前为工作状态时先退出工作态，再进入示忙态
		if (AgentConfig.AGENT_STATE_WORK == getAgentState(user).getResult().getAgentState()) {
			outwork(user);
			return busy(user);
		}
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getSayBusyUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "进入示忙态请求失败", Result.class);
	}
	
	/**
	 * 
	 * @Title：work
	 * @Description: TODO(坐席进入工作)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public AgentHttpResult<Result> work(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getWorkUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "进入工作态请求失败", Result.class);
	}
	
	/**
	 * 
	 * @Title：outwork
	 * @Description: TODO(退出工作)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<Result> outwork(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getCancelWorkUrl().
				replace(AGENT_ID, user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "退出工作态请求失败", Result.class);
	}
	
	/**
	 * 
	 * @Title：logout
	 * @Description: TODO(座席签出)
	 * @see：
	 * @param user
	 * @throws Exception
	 */
	public void logout(User user) throws Exception {
		try {
			HttpClientResult httpClientResult = AgentClientUtil.delete(user, defaultAgentParam.getLogoutUrl().replace(AGENT_ID, 
					user.getAgentUser().getWorkno()), null);
			assertAgent(httpClientResult, "坐席签出请求失败", Result.class);
		} finally {
			clearAgentEvent(user);
			clearGuid(user);
		}
	}
	
	/**
	 * 
	 * @Title：voiceCallOut
	 * @Description: TODO(外呼(默认外呼参数))
	 * @see：
	 * @param user
	 * @param called
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> voiceCallOut(User user, String called) throws Exception {
		CallParam defaultCallParam = defaultAgentParam.getCallParam();
		defaultCallParam.setCalled(called);
		return voiceCallOut(user, defaultCallParam);
	}
	
	/**
	 * 
	 * @Title：voicecallOut
	 * @Description: TODO(外呼)
	 * @see：
	 * @param user
	 * @param callParam
	 * @return 
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> voiceCallOut(User user, CallParam callParam) throws Exception {
		HttpClientResult callResult = AgentClientUtil.put(user, defaultAgentParam.getCallOutUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), callParam);
		AgentHttpResult<String> agentHttpResult = assertAgent(callResult, "坐席外呼请求失败", String.class);
		// 呼叫成功后将被叫号码注入代理数据中，因为 呼叫振铃事件中没有 Called 返回参数
		setAgentEventData(user, AgentConfig.AGENT_DATA_CALLNUM, callParam.getCalled());
		return agentHttpResult;
	}
	
	/**
	 * 
	 * @Title：voiceAnswer
	 * @Description: TODO(应答)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> voiceAnswer(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.put(user, defaultAgentParam.getAnswerUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "应答请求失败", String.class);
	}
	
	/**
	 * 
	 * @Title：phonePickUp
	 * @Description: TODO(话机联动应答)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> phonePickUp(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.put(user, defaultAgentParam.getPhonePickUpUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "话机联动应答请求失败", String.class);
	}
	
	/**
	 * 
	 * @Title：phoneHangUp
	 * @Description: TODO(话机联动拒接)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> phoneHangUp(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.put(user, defaultAgentParam.getPhoneHangUpUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "话机联动拒接请求失败", String.class);
	}
	
	/**
	 * 
	 * @Title：voiceRelease
	 * @Description: TODO(挂断呼叫)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> voiceCallEnd(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.delete(user, defaultAgentParam.getReleaseUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "挂断呼叫请求失败", String.class);
	}

	/**
	 * 
	 * @Title：assertAgent
	 * @Description: TODO(断言接口请求)
	 * @see：
	 * @param httpClientResult
	 * @param assertMsg
	 * @param clazz 消息实体类型
	 * @return
	 */
	private  <T> AgentHttpResult<T> assertAgent(HttpClientResult httpClientResult, String assertMsg, Class<T> clazz) {
		AssertUtil.isTrue(HttpStatus.SC_OK == httpClientResult.getCode(), HttpStatus.AS_ERROR.getAjaxStatus(),
				assertMsg + ": " + httpClientResult.getCode());
		AgentHttpResult<T> agentHttpResult = JSON.parseObject(httpClientResult.getContent(), new TypeReference<AgentHttpResult<T>>(clazz){});
		AssertUtil.isTrue(StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), agentHttpResult.getRetcode()), 
				HttpStatus.AS_ERROR.getAjaxStatus(), agentHttpResult.getMessage());
		return agentHttpResult;
	}
	
	/**
	 * 
	 * @Title：setAgentEventData
	 * @Description: TODO(注入代理数据)
	 * @see：
	 * @param user
	 * @param key
	 * @param object
	 */
	public void setAgentEventData(User user, String key, Object object) {
		if (agentQueue.eventThreadMap.containsKey(user.getId())) {
			agentQueue.eventThreadMap.get(user.getId()).setAgentEventData(key, object);
		}
	}
	
	/**
	 * 
	 * @Title：getAgentEventData
	 * @Description: TODO(取出代理数据)
	 * @see：
	 * @param user
	 * @param key
	 * @return
	 */
	public Object getAgentEventData(User user, String key) {
		return agentQueue.eventThreadMap.containsKey(user.getId()) ? 
				agentQueue.eventThreadMap.get(user.getId()).getAgentEventData(key) : null;
	}
	
	/**
	 * 
	 * @Title：clearAgentEventData
	 * @Description: TODO(清除代理数据)
	 * @see：
	 * @param user
	 */
	public void clearAgentEventData(User user) {
		if (agentQueue.eventThreadMap.containsKey(user.getId())) {
			agentQueue.eventThreadMap.get(user.getId()).clearAgentEventData();
		}
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
	 * @Description: TODO(清除代理事件) 
	 * @see：
	 * @param user
	 */
	public void clearAgentEvent(User user) {
		if (agentQueue.eventThreadMap.containsKey(user.getId())) {
			agentQueue.eventThreadMap.get(user.getId()).end();
			agentQueue.eventThreadMap.remove(user.getId());
		}
	}
	
	/**
	 * 
	 * @Title：clearGuid
	 * @Description: TODO(清除鉴权信息)
	 * @see：
	 * @param user
	 */
	public void clearGuid(User user) {
		if (agentQueue.guidMap.containsKey(user.getId())) {
			agentQueue.guidMap.remove(user.getId());
		}
	}
	
	/**
	 * 
	 * @Title：insertCallData
	 * @Description: TODO(呼叫数据记录)
	 * @see：
	 * @param user
	 * @param entity
	 */
	public void insertCallData(User user, AgentCallData entity) {
		entity.setUser(user);
		entity.setCreateDate(new Date());
		dao.insert(entity);
		setAgentEventData(user, AgentConfig.AGENT_DATA_CALLID, entity.getCallid());
	}
	
	/**
	 * 
	 * @Title：updateCallData
	 * @Description: TODO(更新记录)
	 * @see：
	 * @param entity
	 */
	public void updateCallData(AgentCallData entity) {
		dao.update(entity);
	}
	
	/**
	 * 
	 * @Title：insertRecord
	 * @Description: TODO(录音保存)
	 * @see：
	 * @param user
	 * @param entity
	 */
	public void insertRecord(User user, AgentRecord entity) {
		String callId = (String) getAgentEventData(user, AgentConfig.AGENT_DATA_CALLID);
		if (StringUtils.isBlank(callId)) {
			return;
		}
		AgentCallData agentCallData = new AgentCallData(callId);
		// 更新通话记录
		updateCallData(agentCallData);
		entity.setCreateDate(new Date());
		agentCallData.setAgentRecord(entity);
		dao.insertRecord(agentCallData);
	}
	
	/**
	 * 
	 * @Title：updateRecordEndDate
	 * @Description: TODO(更新录音结束时间)
	 * @see：
	 * @param user
	 */
	public void updateRecordEndDate(User user) {
		String callId = (String) getAgentEventData(user, AgentConfig.AGENT_DATA_CALLID);
		if (StringUtils.isBlank(callId)) {
			return;
		}
		AgentCallData agentCallData = new AgentCallData(callId);
		agentCallData.setAgentRecord(new AgentRecord());
		agentCallData.getAgentRecord().setUpdateDate(new Date());
		dao.updateRecordEndDate(agentCallData);
		clearAgentEventData(user);
	}
	
}
