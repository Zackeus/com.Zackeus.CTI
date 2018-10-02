package com.Zackeus.CTI.modules.agent.demo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.httpClient.HttpClientUtil;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.agent.config.CallParam;
import com.Zackeus.CTI.modules.agent.entity.AgentSocketMsg;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:HttpController
 * @Description:TODO(DEMO演示)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年10月2日 下午1:15:42
 */
@Controller
@RequestMapping("/sys/demo")
public class DemoController extends BaseController {
	
	/**
	 * 
	 * @Title：voiceTest
	 * @Description: TODO(外呼)
	 * @see：
	 * @param agentSocketMsg
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = {"/voiceCallOut"}, method = RequestMethod.GET)
	public String voiceCallOutPage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/demo/callOut";
	}
	
	/***
	 * 
	 * @Title：test
	 * @Description: TODO(接收事件)
	 * @see：
	 * @param agentSocketMsg
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = {"/receive"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			method = RequestMethod.POST)
	public void test(@RequestBody AgentSocketMsg agentSocketMsg, HttpServletRequest request, HttpServletResponse response) {
		Logs.info("当前用户：" + UserUtils.getPrincipal());
		Logs.info(agentSocketMsg);
		renderString(response, new AjaxResult(HttpStatus.SC_OK, "成功"));
	}
	
	/**
	 * 
	 * @Title：voiceTest
	 * @Description: TODO(拨号)
	 * @see：
	 * @param agentSocketMsg
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = {"/voiceCallTest/{called}"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceTest( @PathVariable(value = "called") String called, HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		Map<String, String> heads = new HashMap<>();
		heads.put("cookie", request.getHeader("cookie"));
		
		called = called.startsWith("0") ? called : "0" + called;
		String url = "http://10.5.133.244:8008/com.Zackeus.CTI/sys/httpAgent/voiceCallOut?userId=c69866984eb942f3a67a9ca91ce70953&postUrl=http://10.5.133.244:8008/com.Zackeus.CTI/sys/demo/receive";
		CallParam callParam = new CallParam();
		callParam.setCalled(called);
		HttpClientResult  httpClientResult = HttpClientUtil.doPostJson(url, heads, callParam);
		Logs.info("结果：" + httpClientResult);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "外呼成功"));
	}
	
}
