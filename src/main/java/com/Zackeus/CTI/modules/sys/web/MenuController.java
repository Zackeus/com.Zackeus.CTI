package com.Zackeus.CTI.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Zackeus.CTI.common.entity.LayuiTable;
import com.Zackeus.CTI.common.utils.WebUtils;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.sys.service.MenuService;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:MenuController
 * @Description:TODO(菜单Controller)
 * @Company:
 * @author zhou.zhang
 * @date 2018年8月14日 下午4:38:18
 */
@Controller
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 
	 * @Title：getMenuList
	 * @Description: TODO(根据功能菜单生成左侧菜单树)
	 * @see：
	 * @param id 一级菜单ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/list/{id}")
	public void getMenuList(@PathVariable("id") String id, HttpServletRequest request,
			HttpServletResponse response) {
		renderString(response, UserUtils.getMenuListByUser(id));
	}
	
	/**
	 * 
	 * @Title：menuMange
	 * @Description: TODO(菜单管理)
	 * @see：
	 * @param parentId
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/mange")
	public String menuMange(HttpServletRequest request, HttpServletResponse response) {
		if (WebUtils.isAjaxRequest(request)) {
			renderString(response, new LayuiTable<>(menuService.findAllList()));
			return null;
		}
		return "modules/sys/menu/menuMange";
	}
	
}
