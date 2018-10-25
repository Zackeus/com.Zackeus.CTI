package com.Zackeus.CTI.modules.agent.demo;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.entity.Page;
import com.Zackeus.CTI.common.utils.JsonMapper;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.httpClient.HttpClientUtil;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.agent.config.CallParam;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;
import com.Zackeus.CTI.modules.agent.entity.AgentSocketMsg;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import net.sf.json.JSONObject;

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
	
//	private String URL = "http://10.5.60.66:8989/com.Zackeus.CTI/sys/httpAgent/{mapping}?userId={userId}&postUrl={postUrl}";
	private String URL = "http://10.5.133.244:8008/com.Zackeus.CTI/sys/httpAgent/{mapping}?userId={userId}&postUrl={postUrl}";
	private String userId = StringUtils.EMPTY;
	private String postUrl = StringUtils.EMPTY;
	
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
		Logs.info(JSONObject.fromObject(agentSocketMsg).toString(2));
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
	@RequestMapping(value = {"/voiceCallTest"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceTest(@RequestParam(value = "userId") String userId, @RequestParam(value = "postUrl") String postUrl, 
			@RequestParam(value = "called") String called, HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.userId = userId;
		this.postUrl = postUrl;
		String callUrl = URL.replace("{mapping}", "voiceCallOut").replace("{userId}", this.userId).replace("{postUrl}", this.postUrl);
		
		Map<String, String> heads = new HashMap<>();
		heads.put("cookie", request.getHeader("cookie"));
		
		called = called.startsWith("0") ? called : "0" + called;
		CallParam callParam = new CallParam();
		callParam.setCalled(called);
		
		HttpClientResult  httpClientResult = HttpClientUtil.doPostJson(callUrl, heads, callParam);
		Logs.info("结果：" + httpClientResult);
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
	@RequestMapping(value = {"/voiceCallEndTest"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void voiceCallEnd(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String callEndUrl = URL.replace("{mapping}", "voiceCallEnd").replace("{userId}", this.userId).replace("{postUrl}", this.postUrl);
		
		Map<String, String> heads = new HashMap<>();
		heads.put("cookie", request.getHeader("cookie"));
		
		HttpClientResult  httpClientResult = HttpClientUtil.doPostJson(callEndUrl, heads, null);
		Logs.info("结果：" + httpClientResult);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "挂断呼叫成功"));
	}
	
	/**
	 * 
	 * @Title：callRecordManagePage
	 * @Description: TODO(通话记录列表页面)
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/callRecordManage", method = RequestMethod.GET)
	public String callRecordManagePage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/demo/callRecordManage";
	}
	
	/**
	 * 
	 * @Title：callRecordManage
	 * @Description: TODO(通话记录分页查询)
	 * @see：
	 * @param userId
	 * @param postUrl
	 * @param called
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"/callRecordManage"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void callRecordManage(@RequestParam(value = "userId") String userId, 
			@RequestParam(value = "page") Integer page, 
			@RequestParam(value = "pageSize") Integer pageSize,
			@RequestParam(value = "callid") String callid, 
			@RequestParam(value = "userName") String userName, 
			@RequestParam(value = "type") String type,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.userId = userId;
		String callRecordManageUrl = URL.replace("{mapping}", "callRecordManage").replace("{userId}", this.userId).replace("{postUrl}", StringUtils.EMPTY);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("userId", userId);
		jsonObject.put("page", page);
		jsonObject.put("pageSize", pageSize);
		jsonObject.put("callid", callid);
		jsonObject.put("userName", userName);
		jsonObject.put("type", type);
		
		HttpClientResult httpClientResult  = HttpClientUtil.doPostJson(callRecordManageUrl, jsonObject);
		Logs.info("结果：" + httpClientResult.getCode());
		if (HttpStatus.SC_OK == httpClientResult.getCode()) {
			AjaxResult ajaxResult = JSON.parseObject(httpClientResult.getContent(), AjaxResult.class);
			if (HttpStatus.SC_SUCCESS == ajaxResult.getCode()) {
				Page<AgentCallData> pageData = JSON.parseObject(JsonMapper.toJsonString(ajaxResult.getCustomObj()), 
						new TypeReference<Page<AgentCallData>>(AgentCallData.class){});
				Logs.info("条数：" + pageData.getList().size());
				Logs.info("数据： " + pageData);
			}
		}
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "外呼成功"));
	}
}
