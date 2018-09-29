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

import com.Zackeus.CTI.common.annotation.argumentResolver.RequestAttribute;
import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseHttpController;
import com.Zackeus.CTI.modules.agent.config.CallParam;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.sys.entity.Principal;
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
	public void voiceCallOut(@RequestAttribute(name = "agentUser") Principal agentUser, @Validated @RequestBody CallParam callParam, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		callParam.setCalled(callParam.getCalled().startsWith("0") ? callParam.getCalled() : "0" + callParam.getCalled());
		agentService.voiceCallOut(new User(agentUser), callParam);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "外呼成功"));
	}

}
