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
import org.springframework.web.bind.annotation.RequestParam;

import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.entity.HttpClientResult;
import com.Zackeus.CTI.common.entity.Page;
import com.Zackeus.CTI.common.utils.AssertUtil;
import com.Zackeus.CTI.common.utils.JsonMapper;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.httpClient.HttpClientUtil;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.agent.config.CallParam;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;
import com.Zackeus.CTI.modules.agent.entity.AgentRecord;
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
	 * @Description: TODO(演示页面)
	 * @see：
	 * @param agentSocketMsg
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = {"/demopage"}, method = RequestMethod.GET)
	public String voiceCallOutPage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/demo/demo";
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
		// 前端响应信息
		AjaxResult ajaxResult = new AjaxResult(HttpStatus.SC_SUCCESS, "查询成功");
		
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
			AjaxResult ajaxResultData = JSON.parseObject(httpClientResult.getContent(), AjaxResult.class);
			if (HttpStatus.SC_SUCCESS == ajaxResultData.getCode()) {
				Page<AgentCallData> pageData = JSON.parseObject(JsonMapper.toJsonString(ajaxResultData.getCustomObj()), 
						new TypeReference<Page<AgentCallData>>(AgentCallData.class){});
				ajaxResult.setMsg("查询成功，条数：" + pageData.getList().size());
				Logs.info("条数：" + pageData.getList().size());
				Logs.info("数据： " + pageData);
			} else {
				ajaxResult = new AjaxResult(ajaxResultData.getCode(), "查询失败， 信息：" + ajaxResultData.getMsg());
			}
		} else {
			ajaxResult = new AjaxResult(httpClientResult.getCode(), "查询失败， 信息：" + httpClientResult.getContent());
		}
		renderString(response, ajaxResult);
	}
	
	/**
	 * 
	 * @Title：recordPlay
	 * @Description: TODO(录音回放)
	 * @see：
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = {"/recordPlay/{userId}"}, consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void recordPlay(@PathVariable("userId") String userId, @RequestBody AgentRecord agentRecord, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.userId = userId;
		String recordPlayUrl = URL.replace("{mapping}", "recordPlay").replace("{userId}", this.userId).replace("{postUrl}", StringUtils.EMPTY);
		
		HttpClientResult httpClientResult  = HttpClientUtil.doPostJson(recordPlayUrl, agentRecord);
		Logs.info("结果：" + httpClientResult.getCode());
		
		AssertUtil.isTrue(HttpStatus.SC_OK == httpClientResult.getCode(), httpClientResult.getCode(), "操作失败");
		AjaxResult ajaxResult =  JSON.parseObject(httpClientResult.getContent(), AjaxResult.class);
		AssertUtil.isTrue(HttpStatus.SC_SUCCESS == ajaxResult.getCode(), ajaxResult.getCode(), ajaxResult.getMsg());
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, ajaxResult.getMsg()));
	}
}
