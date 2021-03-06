package com.Zackeus.CTI.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.WebUtils;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.BaseController;
import com.Zackeus.CTI.modules.sys.entity.Principal;
import com.Zackeus.CTI.modules.sys.security.LoginAuthenticationFilter;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:LoginController
 * @Description:TODO(登录Controller)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月12日 下午4:17:02
 */
@Controller
@RequestMapping("/sys")
public class LoginController extends BaseController {
	
	/**
	 * 
	 * @Title：login
	 * @Description: TODO(登录管理) 
	 * @see：
	 * @param session
	 * @param request
	 * @param response 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		return !ObjectUtils.isEmpty(principal) && !principal.isMobileLogin() ? "redirect:" + "/sys/area/index"
				: "modules/sys/login";
	}
	
	/**
	 * 
	 * @Title：loginFail
	 * @Description: TODO(登录失败，真正登录的POST请求由Filter完成) 
	 * @see：
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public void loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if (principal != null) {
			renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "已经登录"));
			return;
		}
		String message = (String) request.getAttribute(LoginAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")) {
			message = "用户或密码错误, 请重试.";
		}
		renderString(response, new AjaxResult(HttpStatus.SC_LOGIN_ERROR, message));
	}
	
	/**
	 * 
	 * @Title：index
	 * @Description: TODO(登录成功，进入管理首页) 
	 * @see：
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/loginSuccess", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String loginSuccess(HttpServletRequest request, HttpServletResponse response, Model model) {
		if (WebUtils.isAjaxRequest(request)) {
			renderString(response, new AjaxResult(HttpStatus.SC_SUCCESS, "登录成功"));
			return null;
		}
		return "modules/sys/sysIndex";
	}
	
	/**
	 * 
	 * @Title：logout
	 * @Description: TODO(用户退出)
	 * @see：
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		if (!ObjectUtils.isEmpty(principal)) {
			UserUtils.getSubject().logout();
		}
		return "redirect:" + "/sys/login";
	}

}
