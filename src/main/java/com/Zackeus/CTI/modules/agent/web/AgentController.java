package com.Zackeus.CTI.modules.agent.web;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.annotation.argumentResolver.PageRequestBody;
import com.Zackeus.CTI.common.annotation.validator.CallNum;
import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.entity.Page;
import com.Zackeus.CTI.common.utils.ExcelUtil;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.agent.config.AgentConfig;
import com.Zackeus.CTI.modules.agent.entity.AgentCallData;
import com.Zackeus.CTI.modules.agent.entity.AgentRecord;
import com.Zackeus.CTI.modules.agent.entity.CallDataExport;
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
	 * @Description: TODO(弹屏页面)
	 * @see：
	 * @param eventCode 事件类型码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = {"/popUp/{eventCode}/{callNum}"}, method = RequestMethod.GET)
	public String popUp(@PathVariable("eventCode") int eventCode, @PathVariable("callNum") String callNum,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("callNum", callNum);
		switch (eventCode) {
		case AgentConfig.EVENT_VOICE_CALL:
			model.addAttribute("callEvent", "正在呼叫");
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
	
	/**
	 * 
	 * @Title：recordList
	 * @Description: TODO(通话记录列表页面)
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/callRecordManage", method = RequestMethod.GET)
	public String callRecordManagePage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/agent/voiceCall/callRecordManage";
	}
	
	/**
	 * 
	 * @Title：recordManage
	 * @Description: TODO(通话记录分页查询)
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/callRecordManage", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void callRecordManage(@PageRequestBody AgentCallData agentCallData, 
			HttpServletRequest request, HttpServletResponse response) {
		agentCallData.setCurrentUser(new User(UserUtils.getPrincipal()));
		renderString(response, agentService.findCallRecordPage(new Page<>(request), agentCallData));
	}
	
	/**
	 * 
	 * @Title：recordPlayPage
	 * @Description: TODO(录音回放页面)
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/recordPlayPage/{recordID}/{recordTitle}", method = RequestMethod.GET)
	public String recordPlayPage(@PathVariable("recordID") String recordID, @PathVariable("recordTitle") String recordTitle,
			HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("recordTitle", recordTitle);
		model.addAttribute("album", recordID + ".v3");
		return "modules/agent/record/recordPlay";
	}
	
	/**
	 * 
	 * @Title：recordPlay
	 * @Description: TODO(录音回放)
	 * @see：
	 * @param agentCallData
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/recordPlay", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void recordPlay(@Validated @RequestBody AgentRecord agentRecordData, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		AjaxResult ajaxResult = new AjaxResult(HttpStatus.SC_SUCCESS, "录音操作成功");
		
		switch (agentRecordData.getControlSign()) {
		case AgentConfig.AGENT_RECORD_PLAY:
			// 录音回放
			AgentRecord agentRecord = agentService.getAgentRecord(agentRecordData);
			if (ObjectUtils.isEmpty(agentRecord)) {
				ajaxResult.setCode(HttpStatus.SC_UNKNOWN);
				ajaxResult.setMsg("查无此数据");
			}  else {
				agentService.recordPlay(new User(UserUtils.getPrincipal()), agentRecord);
				ajaxResult.setMsg("录音回放成功");
			}
			break;
			
		case AgentConfig.AGENT_RECORD_STOP_PLAY:
			// 停止放音
			agentService.recordStopPlay(new User(UserUtils.getPrincipal()));
			ajaxResult.setMsg("停止放音成功");
			break;
			
		case AgentConfig.AGENT_RECORD_PAUSE_RECORD:
			// 暂停放音
			agentService.recordPausePlay(new User(UserUtils.getPrincipal()));
			ajaxResult.setMsg("暂停放音成功");
			break;
			
		case AgentConfig.AGENT_RECORD_RESUME_RECORD:
			// 恢复放音
			agentService.recordResumePlay(new User(UserUtils.getPrincipal()));
			ajaxResult.setMsg("恢复放音成功");
			break;
			
		case AgentConfig.AGENT_RECORD_FORE_FAST:
			// 放音快进
			agentService.recordForeFast(new User(UserUtils.getPrincipal()), String.valueOf(agentRecordData.getFastTime()));
			ajaxResult.setMsg("放音快进成功");
			break;
			
		case AgentConfig.AGENT_RECORD_BACK_FAST:
			// 放音快退
			agentService.recordBackFast(new User(UserUtils.getPrincipal()), String.valueOf(agentRecordData.getFastTime()));
			ajaxResult.setMsg("放音快退成功");
			break;

		default:
			ajaxResult = new AjaxResult(HttpStatus.SC_BAD_REQUEST, "无效的录音控制标识");
			break;
		}
		renderString(response, ajaxResult);
	}
	
	/**
	 * 
	 * @Title：callDataExportPage
	 * @Description: TODO(通话报表导出页面)
	 * @see：
	 * @param request
	 * @param response
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/callDataExport", method = RequestMethod.GET)
	public String callDataExportPage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/agent/voiceCall/callDataExport";
	}
	
	/**
	 * 
	 * @Title：callRecordExport
	 * @Description: TODO(通话报表导出)
	 * @see：
	 * @param agentCallData
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/callDataExport", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void callDataExport(@RequestBody CallDataExport callDataExport, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
//		consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
		
		XSSFWorkbook secondWb = ExcelUtil.exportExcel2007(agentService.getCallDataByExport(callDataExport), 
				AgentCallData.class, null);
		ExcelUtil.writeExcel("test.xlsx", response, secondWb);
	}
	
}
