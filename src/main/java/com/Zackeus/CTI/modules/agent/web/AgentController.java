package com.Zackeus.CTI.modules.agent.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.annotation.validator.CallNum;
import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.agent.config.AgentConfig;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:AgentController
 * @Description:TODO(坐席Controller)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月25日 下午3:14:23
 */
@Controller
@RequestMapping("/sys/agent")
@Validated
public class AgentController extends BaseController {
	
	@Autowired
	private AgentService agentService;
	
	/**
	 * 
	 * @Title：popUp
	 * @Description: TODO(弹框页面)
	 * @see：
	 * @param eventCode 事件类型码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"/popUp/{eventCode}"}, method = RequestMethod.GET)
	public String popUp(@PathVariable("eventCode") int eventCode, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = new User(UserUtils.getPrincipal());
		switch (eventCode) {
		case AgentConfig.EVENT_VOICE_CALL:
			model.addAttribute("callEvent", "正在呼叫");
			model.addAttribute("callNum", agentService.getCalled(user));
			return "modules/agent/voiceCall/callOutPopUp";
		case AgentConfig.EVENT_VOICE_RING:
			model.addAttribute("callEvent", "来电振铃");
			return "modules/agent/voiceCall/callOutPopUp";
		default:
			return "error/404";
		}
	}
	
	/**
	 * 
	 * @Title：changeAgentState
	 * @Description: TODO(切换坐席状态)
	 * @see：
	 * @param agentState
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/changeAgentState/{agentState}")
	public void changeAgentState(@PathVariable("agentState") int agentState, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		User user = new User(UserUtils.getPrincipal());
		switch (agentState) {
		
		case AgentConfig.AGENT_STATE_FREE:
			// 示闲
			agentService.ready(user);
			break;
			
		case AgentConfig.AGENT_STATE_BUSY:
			// 示忙
			agentService.busy(user);
			break;
			
		case AgentConfig.AGENT_STATE_WORK:
			// 工作
			agentService.work(user);
			break;

		default:
			throw new MyException(HttpStatus.AS_ERROR.getAjaxStatus(), "无效的状态码");
		}
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "状态切换成功"));
	}
	
	/**
	 * 
	 * @Title：voiceCallOut
	 * @Description: TODO(外呼)
	 * @see：
	 * @param called
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"/voiceCallOut"}, method = RequestMethod.GET)
	public String voiceCallOutPage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/agent/voiceCall/callOut";
	}
	
	/**
	 * 
	 * @Title：voiceCallOut
	 * @Description: TODO(外呼)
	 * @see：
	 * @param called
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"/voiceCallOut/{called}"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceCallOut(@CallNum @PathVariable(value = "called") String called, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		called = called.startsWith("0") ? called : "0" + called;
		agentService.voiceCallOut(new User(UserUtils.getPrincipal()), called);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "外呼成功"));
	}
	
	/**
	 * 
	 * @Title：voiceCallAnswer
	 * @Description: TODO(应答)
	 * @see：
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"/voiceCallAnswer"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceCallAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		agentService.voiceAnswer(new User(UserUtils.getPrincipal()));
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "应答成功"));
	}
	
	/**
	 * 
	 * @Title：voiceCallRefuse
	 * @Description: TODO(拒接)
	 * @see：
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"/voiceCallRefuse"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceCallRefuse(HttpServletRequest request, HttpServletResponse response) throws Exception {
		agentService.phoneHangUp(new User(UserUtils.getPrincipal()));
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "拒接成功"));
	}
	
	/**
	 * 
	 * @Title：voiceCallEnd
	 * @Description: TODO(挂断呼叫)
	 * @see：
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"/voiceCallEnd"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceCallEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		agentService.voiceCallEnd(new User(UserUtils.getPrincipal()));
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "挂断呼叫成功"));
	}
	
}
