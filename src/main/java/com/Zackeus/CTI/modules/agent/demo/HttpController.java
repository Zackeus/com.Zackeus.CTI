package com.Zackeus.CTI.modules.agent.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.agent.entity.AgentSocketMsg;

@Controller
@RequestMapping("/sys/demo")
public class HttpController extends BaseController {
	
	@RequestMapping(value = {"/receive"}, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			method = RequestMethod.POST)
	public void test(@RequestBody AgentSocketMsg agentSocketMsg, HttpServletRequest request, HttpServletResponse response) {
		Logs.info(agentSocketMsg);
		renderString(response, new AjaxResult(HttpStatus.SC_OK, "成功"));
	}

}
