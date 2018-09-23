package com.Zackeus.CTI.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
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
import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.entity.Page;
import com.Zackeus.CTI.common.service.valid.UpdateVaild;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.service.UserService;

/**
 * 
 * @Title:UserController
 * @Description:TODO(用户Controller)
 * @Company:
 * @author zhou.zhang
 * @date 2018年8月15日 上午9:25:13
 */
@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;
	
	/**
	 * 
	 * @Title：userMange
	 * @Description: TODO(用户管理) 
	 * @see：
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/manage", method = RequestMethod.GET)
	public String userManage(HttpServletRequest request, HttpServletResponse response) {
		return "modules/sys/user/userManage";
	}

	/**
	 * 
	 * @Title：userList
	 * @Description: TODO(用户分页条件查询) 
	 * @see：
	 * @param user
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void userList(@PageRequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		renderString(response, userService.findPage(new Page<>(request), user));
	}

	/**
	 * 
	 * @Title：editUserPage
	 * @Description: TODO(编辑用户页面) 
	 * @see：
	 * @param id
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String editUserPage(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		model.addAttribute("user", userService.get(id));
		return "modules/sys/user/editUser";
	}

	/**
	 * 
	 * @Title：editUser
	 * @Description: TODO(编辑用户) 
	 * @see：
	 * @param user
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void editUser(@Validated({ UpdateVaild.class }) @RequestBody User user, HttpServletRequest request,
			HttpServletResponse response) {
		userService.save(user);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "更新用户成功"));
	}

	/**
	 * 
	 * @Title：cancelUser
	 * @Description: TODO(注销用户) 
	 * @see：
	 * @param id
	 * @param user
	 * @param request
	 * @param response
	 */
	@RequiresRoles(value = { "admin" })
	@RequestMapping(value = "/cancel", consumes = MediaType.APPLICATION_JSON_VALUE, 
		produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void cancelUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
		userService.cancelUser(user);
		renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "注销用户成功"));
	}

}
