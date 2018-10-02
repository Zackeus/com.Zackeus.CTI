package com.Zackeus.CTI.modules.agent.security;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Autowired;

import com.Zackeus.CTI.common.entity.AjaxResult;
import com.Zackeus.CTI.common.security.BaseFilter;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.PatternUtil;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.WebUtils;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.common.web.MyHttpServletRequestWrapper;
import com.Zackeus.CTI.modules.agent.config.AgentConfig;
import com.Zackeus.CTI.modules.agent.service.AgentService;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.service.SystemService;
import com.Zackeus.CTI.modules.sys.service.UserService;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:HttpAgentFilter
 * @Description:TODO(坐席接口拦截器)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月28日 下午2:02:06
 */
public class HttpAgentFilter extends BaseFilter {
	
	@Autowired
	SystemService systemService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	AgentService agentService;
	
	protected void executeChain(User user, ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
		request.setAttribute("user", user);
		super.executeChain(request, response, chain);
	}
	
	@Override
	public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		if (StringUtils.equals(HttpPost.METHOD_NAME, WebUtils.toHttp(request).getMethod())) {
			request = new MyHttpServletRequestWrapper(WebUtils.toHttp(request));
		}
		Exception exception = null;
        try {
            User user = preHandle(request, response, chain);
            UserUtils.kickOutSysUser(user, HttpStatus.SC_SESSION_HTTPLOGIN);
            postHandle(request, response);
        } catch (Exception e) {
            exception = e;
        } finally {
            cleanup(request, response, exception);
        }
	}
	
	/**
	 * 前置通知
	 */
	protected User preHandle(ServletRequest request, ServletResponse response, FilterChain chain) throws Exception {
		String cookie = analysisHeader(request, AgentConfig.AGENT_DATA_COOKIE);
		User user = new User();
		Map<String, String> urlParam = WebUtils.getRequestQuery(WebUtils.toHttp(request).getQueryString());
		if (ObjectUtils.isEmpty(urlParam) || StringUtils.isBlank(urlParam.get(AgentConfig.AGENT_HTTP_USERID))) {
			throw new MyException(HttpStatus.SC_LOGIN_ERROR, "用户凭证不能为空");
		}
		
		//	事件推送地址
		String postUrl = StringUtils.EMPTY;
		if (StringUtils.isNotBlank(urlParam.get(AgentConfig.AGENT_HTTP_POSTURL))) {
			if (!PatternUtil.check(PatternUtil.PATTEN_URL, urlParam.get(AgentConfig.AGENT_HTTP_POSTURL))) {
				throw new MyException(HttpStatus.SC_LOGIN_ERROR, "无效的推送格式");
			}
			postUrl = urlParam.get(AgentConfig.AGENT_HTTP_POSTURL);
		}
		// 账号已经在线
		user = agentService.getAgentEventUser(urlParam.get(AgentConfig.AGENT_HTTP_USERID));
		if (ObjectUtils.isNotEmpty(user)) {
			// 更新COOKIE
			agentService.setAgentEventData(user, AgentConfig.AGENT_DATA_COOKIE, cookie);
			// 更新推送地址
			agentService.setAgentEventData(user, AgentConfig.AGENT_DATA_POSTURL, postUrl);
			// 更新事件时间
			agentService.setAgentEventData(user, AgentConfig.AGENT_DATA_EVENTDATE, new Date());
			// 更改登录标志为接口登录
			agentService.setAgentEventData(user, AgentConfig.AGENT_DATA_ISHTTP, Boolean.TRUE);
			executeChain(user, request, response, chain);
			return user;
		}
		
		user = systemService.getUserById(urlParam.get(AgentConfig.AGENT_HTTP_USERID));
    	if (ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getAgentUser()) || StringUtils.isBlank(user.getAgentUser().getWorkno())) {
    		throw new MyException(HttpStatus.SC_LOGIN_ERROR, "账号未注册");
		}
      	if (StringUtils.isBlank(user.getAgentUser().getPhonenumber())) {
      		throw new MyException(HttpStatus.SC_LOGIN_ERROR, "账号未绑定座机号");
		}
    	if (ObjectUtils.isNotEmpty(userService.getByAgentWorkNo(user))) {
    		throw new MyException(HttpStatus.SC_LOGIN_ERROR, "坐席工号:" + user.getAgentUser().getWorkno() + "已被多个用户注册，请联系管理员");
		}
    	if (ObjectUtils.isNotEmpty(userService.getByPhone(user))) {
    		throw new MyException(HttpStatus.SC_LOGIN_ERROR, "座机号:" + user.getAgentUser().getPhonenumber() + "已被多个用户占用，请联系管理员");
		}
    	agentService.login(user);
    	agentService.addAgentEvent(user, cookie, postUrl, Boolean.TRUE);
		executeChain(user, request, response, chain);
		return user;
	}
	
	/**
	 * 后置通知
	 */
	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
		super.postHandle(request, response);
	}
	
	/**
	 * 最终通知
	 */
	@Override
	public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception)
			throws Exception {
		super.afterCompletion(request, response, exception);
	}
	
	/**
	 * 异常处理
	 */
	@Override
	protected void cleanup(ServletRequest request, ServletResponse response, Exception existing) {
		Exception exception = existing;
		try {
			afterCompletion(request, response, exception);
		} catch (Exception e) {
			exception = e;
		}
		if (ObjectUtils.isNotEmpty(exception)) {
			if (exception instanceof MyException) {
				handleHttpRequest(response, new AjaxResult(((MyException) exception).getErrorCode(), exception.getMessage()));
            } else {
				handleHttpRequest(response, new AjaxResult(HttpStatus.SC_LOGIN_ERROR, exception.getMessage()));
            }
		}
	}

}
