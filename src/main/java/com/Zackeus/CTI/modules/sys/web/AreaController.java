package com.Zackeus.CTI.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.agent.utils.AgentEventUtil;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:AreaController
 * @Description:TODO(区域Controller)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月14日 下午3:55:54
 */
@Controller
@RequestMapping("/sys/area")
public class AreaController extends BaseController {
	
	@Autowired
	private AgentService agentService;
	
	/**
	 * 
	 * @Title：sysIndex
	 * @Description: TODO(管理主页面)
	 * @see：
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/index")
	public String sysIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("utilityMenus", UserUtils.getMenuListByUser("1"));
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 
	 * @Title：loginSuccess
	 * @Description: TODO(管理副页面)
	 * @see：
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/main")
	public String sysMain(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		model.addAttribute("agentState", AgentEventUtil.analyzeState(agentService.getAgentState(new User(UserUtils.getPrincipal())).getResult()));
		return "modules/sys/sysMain";
	}
	
	/**
	 * 
	 * @Title：icon
	 * @Description: TODO(图标设置)
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/icon", method = RequestMethod.GET)
	public String icon(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/icon";
	}

}
