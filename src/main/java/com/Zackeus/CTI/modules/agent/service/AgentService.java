package com.Zackeus.CTI.modules.agent.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.entity.Page;
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
import com.Zackeus.CTI.modules.agent.config.RecordPlayParam;
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
	private static String FAST_TIME = "{fasttime}";
	
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
		if (StringUtils.equals(HttpStatus.AS_LOGIN.getAgentStatus(), loginResult.getRetcode())) {
			forceLogin(user, loginParam);
			resetskill(user);
			return;
		}
		throw new MyException(retcodeToAjaxcode(loginResult.getRetcode()), loginResult.getMessage());
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
		// 坐席已登出 登出账号
		if (StringUtils.equals(HttpStatus.AS_LOGOUT.getAgentStatus(), agentHttpEvent.getRetcode())) {
			UserUtils.kickOutUser(user, HttpStatus.SC_SESSION_AGENTLOGOUT);
		}
		AssertUtil.isTrue(StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), agentHttpEvent.getRetcode()), 
				retcodeToAjaxcode(agentHttpEvent.getRetcode()), agentHttpEvent.getMessage());
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
			AssertUtil.isTrue(HttpStatus.SC_OK == httpClientResult.getCode(), HttpStatus.AS_ERROR.getAjaxStatus(),
					"坐席签出请求失败: " + httpClientResult.getCode());
			AgentHttpResult<Result> agentHttpResult = JSON.parseObject(httpClientResult.getContent(), new TypeReference<AgentHttpResult<Result>>(Result.class){});
			// 无权限调用接口 表明坐席没有登录 
			if (StringUtils.equals(HttpStatus.AS_NO_AUTHORITY.getAgentStatus(), agentHttpResult.getRetcode())) {
				return;
			}
			AssertUtil.isTrue(StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), agentHttpResult.getRetcode()), 
					retcodeToAjaxcode(agentHttpResult.getRetcode()), agentHttpResult.getMessage());
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
	 * @Title：recordPlay
	 * @Description: TODO(录音回放(默认参数))
	 * @see：
	 * @param user
	 * @param agentRecord
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> recordPlay(User user, AgentRecord agentRecord) throws Exception {
		RecordPlayParam defaultRecordPlayParam = defaultAgentParam.getRecordPlayParam();
		defaultRecordPlayParam.setVoicepath(agentRecord.getFileName());
		return recordPlay(user, agentRecord, defaultRecordPlayParam);
	}
	
	/**
	 * 
	 * @Title：recordPlay
	 * @Description: TODO(录音回放)
	 * @see：
	 * @param user
	 * @param recordPlayParam
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> recordPlay(User user, AgentRecord agentRecord, RecordPlayParam recordPlayParam) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.put(user, defaultAgentParam.getRecordPlayUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), recordPlayParam);
		AgentHttpResult<String> agentHttpResult = assertAgent(httpClientResult, "录音回放请求失败", String.class);
		// 回放成功后将回放录音数据注入代理数据中，供推送和事件判断使用
		setAgentEventData(user, AgentConfig.AGENT_DATA_RECORD, agentRecord);
		return agentHttpResult;
	}
	
	/**
	 * 
	 * @Title：recordStopPlay
	 * @Description: TODO(停止放音)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> recordStopPlay(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.delete(user, defaultAgentParam.getRecordStopPlayUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "停止放音请求失败", String.class);
	}
	
	/**
	 * 
	 * @Title：recordPausePlay
	 * @Description: TODO(暂停放音)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> recordPausePlay(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getRecordPausePlayUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "暂停放音请求失败", String.class);
	}
	
	/**
	 * 
	 * @Title：recordResumePlay
	 * @Description: TODO(恢复放音)
	 * @see：
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> recordResumePlay(User user) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getRecordResumePlayUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()), null);
		return assertAgent(httpClientResult, "恢复放音请求失败", String.class);
	}
	
	/**
	 * 
	 * @Title：recordForeFast
	 * @Description: TODO(放音快进)
	 * @see：
	 * @param user
	 * @param fastTime
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> recordForeFast(User user, String fastTime) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getRecordForeFastUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()).replace(FAST_TIME, fastTime), null);
		return assertAgent(httpClientResult, "放音快进请求失败", String.class);
	}
	
	/**
	 * 
	 * @Title：recordBackFast
	 * @Description: TODO(放音快退)
	 * @see：
	 * @param user
	 * @param fastTime
	 * @return
	 * @throws Exception
	 */
	public AgentHttpResult<String> recordBackFast(User user, String fastTime) throws Exception {
		HttpClientResult httpClientResult = AgentClientUtil.post(user, defaultAgentParam.getRecordBackFastUrl().replace(AGENT_ID, 
				user.getAgentUser().getWorkno()).replace(FAST_TIME, fastTime), null);
		return assertAgent(httpClientResult, "放音快退请求失败", String.class);
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
	private <T> AgentHttpResult<T> assertAgent(HttpClientResult httpClientResult, String assertMsg, Class<T> clazz) {
		AssertUtil.isTrue(HttpStatus.SC_OK == httpClientResult.getCode(), HttpStatus.AS_ERROR.getAjaxStatus(),
				httpClientResult.getCode() + " : " + assertMsg);
		AgentHttpResult<T> agentHttpResult = JSON.parseObject(httpClientResult.getContent(), new TypeReference<AgentHttpResult<T>>(clazz){});
		AssertUtil.isTrue(StringUtils.equals(HttpStatus.AS_SUCCESS.getAgentStatus(), agentHttpResult.getRetcode()), 
				retcodeToAjaxcode(agentHttpResult.getRetcode()), agentHttpResult.getMessage());
		return agentHttpResult;
	}
	
	/**
	 * 
	 * @Title：retcodeToAjaxcode
	 * @Description: TODO(坐席状态吗转HTTP状态吗)
	 * @see：
	 * @param retcode
	 * @return
	 */
	private int retcodeToAjaxcode(String retcode) {
		return StringUtils.isBlank(retcode) ? HttpStatus.AS_ERROR.getAjaxStatus() : 
			Integer.parseInt(retcode.replace(String.valueOf(StringUtils.SEPARATOR_FIRST), StringUtils.EMPTY));
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
		// 注入前清理代理数据
		clearAgentEventData(user);
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
	 * @Title：assertAgentEvent
	 * @Description: TODO(获取代理事件用户对象)
	 * @see：
	 * @param user
	 */
	public User getAgentEventUser(String userId) {
		if (agentQueue.eventThreadMap.containsKey(userId)) {
			return ObjectUtils.isEmpty(agentQueue.eventThreadMap.get(userId)) ? null : agentQueue.eventThreadMap.get(userId).getUser();
		}
		return null;
	}
	
	/**
	 * 
	 * @Title：addQueue
	 * @Description: TODO(启动代理队列) 
	 * @see：
	 * @param user
	 */
	public void addAgentEvent(User user) {
		addAgentEvent(user, StringUtils.EMPTY, StringUtils.EMPTY, Boolean.FALSE);
	}
	
	public void addAgentEvent(User user, String cookie, String postUrl, Boolean isHttp) {
		AgentEventThread agentEventThread = new AgentEventThread(user, cookie, postUrl, isHttp);
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
	 * @Title：getRecordByCallId
	 * @Description: TODO(根据呼叫流水号查询录音)
	 * @see：
	 * @param callId
	 */
	public AgentRecord getRecordByCallId(String callId) {
		return dao.getRecordByCallId(callId);
	}
	
	/**
	 * 
	 * @Title：getAgentRecord
	 * @Description: TODO(根据呼叫或录音流水号查询录音)
	 * @see：
	 * @param agentCallData
	 * @return
	 */
	public AgentRecord getAgentRecord(AgentRecord agentCallDataData) {
		AgentRecord agentRecord = dao.getRecordByCallId(agentCallDataData.getCallid());
		if (ObjectUtils.isEmpty(agentRecord)) {
			if (StringUtils.isBlank(agentCallDataData.getRecordID())) {
				return null;
			}
			return dao.getRecordByRecordID(agentCallDataData.getRecordID());
		}
		return agentRecord;
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
	public AgentRecord updateRecordEndDate(User user) {
		try {
			String callId = (String) getAgentEventData(user, AgentConfig.AGENT_DATA_CALLID);
			if (StringUtils.isBlank(callId)) {
				return null;
			}
			AgentRecord agentRecord = getRecordByCallId(callId);
			agentRecord.setUpdateDate(new Date());
			dao.updateRecordEndDate(new AgentCallData(agentRecord));
			return agentRecord;
		} finally {
			clearAgentEventData(user);
		}
	}
	
	/**
	 * 
	 * @Title：findCallRecordPage
	 * @Description: TODO(通话记录分页查询)
	 * @see：
	 * @param page
	 * @param agentCallData
	 * @return
	 */
	public Page<AgentCallData> findCallRecordPage(Page<AgentCallData> page, AgentCallData agentCallData) {
		agentCallData.setPage(page);
		page.setList(dao.findCallRecordList(agentCallData));
		return page;
	}
	
}
