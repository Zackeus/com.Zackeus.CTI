package com.Zackeus.CTI.modules.agent.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.annotation.argumentResolver.PageRequestBody;
import com.Zackeus.CTI.common.annotation.argumentResolver.RequestAttribute;
import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.entity.Page;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseHttpController;
import com.Zackeus.CTI.modules.agent.config.CallParam;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.sys.entity.User;

/**
 * 
 * @Title:AgentHttpController
 * @Description:TODO(坐席接口Controller)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月28日 下午1:45:07
 */
@Controller
@RequestMapping("/sys/httpAgent")
@Validated
public class AgentHttpController extends BaseHttpController {
	
	@Autowired
	private AgentService agentService;
	
	/**
	 * 
	 * @Title：voiceCallOut
	 * @Description: TODO(外呼)
	 * @see：
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"/voiceCallOut"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			method = RequestMethod.POST)
	public void voiceCallOut(@RequestAttribute(name = "user") User user, @Validated @RequestBody CallParam callParam, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		callParam.setCalled(callParam.getCalled().startsWith("0") ? callParam.getCalled() : "0" + callParam.getCalled());
		agentService.voiceCallOut(user, callParam);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "外呼成功"));
	}
	
	/**
	 * 
	 * @Title：voiceCallEnd
	 * @Description: TODO(挂断)
	 * @see：
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"/voiceCallEnd"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceCallEnd(@RequestAttribute(name = "user") User user, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		agentService.voiceCallEnd(user);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "挂断呼叫成功"));
	}
	
	/**
	 * 
	 * @Title：callRecordManage
	 * @Description: TODO(通话记录分页查询)
	 * @see：
	 * @param user
	 * @param agentCallData
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"/callRecordManage"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void callRecordManage(@RequestAttribute(name = "user") User user, @PageRequestBody AgentCallData agentCallData,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		agentCallData.setCurrentUser(user);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "通话记录分页查询成功", 
				agentService.findCallRecordPage(new Page<>(request), agentCallData)));
	}
	
}
