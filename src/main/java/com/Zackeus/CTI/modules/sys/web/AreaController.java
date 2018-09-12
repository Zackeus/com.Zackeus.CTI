package com.Zackeus.CTI.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Zackeus.CTI.common.web.BaseController;

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
	
	/**
	 * 
	 * @Title：sysIndex
	 * @Description: TODO(管理主页面)
	 * @see：
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/index")
	public String sysIndex(HttpServletRequest request, HttpServletResponse response, Model model) {
		// 获取用户菜单树列表
//		model.addAttribute("treeMenus", UserUtils.getTreeMenusByUser(UserUtils.getPrincipal()));
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
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/main")
	public String sysMain(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/sysMain";
	}

}
