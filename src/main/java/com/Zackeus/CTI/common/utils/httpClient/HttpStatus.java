package com.Zackeus.CTI.common.utils.httpClient;

import org.springframework.web.socket.CloseStatus;

import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.modules.agent.entity.AgentHttpStatus;

/**
 * 
 * @Title:HttpStatus
 * @Description:TODO(请求状态码)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月18日 上午10:33:52
 */
public class HttpStatus implements org.apache.http.HttpStatus {
	/**
	 * -1 ：登录校验异常
	 * 400 ：参数异常
	 * 403 ：授权登录异常
	 * 405 ：不支持当前请求方法
	 * 415 ：不支持当前媒体类型 
	 * 500 ：业务逻辑异常
	 * 999 ：未知的错误
	 * 1001 ：数据库异常
	 */
	
	/**
	 * 成功状态
	 */
	public static final int SC_SUCCESS = 0;
	
	/**
	 * 登录异常
	 */
	public static final int SC_LOGIN_ERROR = -1;
	
	/**
	 * 异地登录
	 */
	public static final CloseStatus SC_KICK_OUT = new CloseStatus(4600, "账号异地登录！");
	
	/**
	 * 会话超时
	 */
	public static final CloseStatus SC_SESSION_EXPRIES = new CloseStatus(4610, "长时间未操作！");
	
	/**
	 * 接口登录
	 */
	public static final CloseStatus SC_SESSION_HTTPLOGIN = new CloseStatus(4620, "接口登录！");
	
	/**
	 * 坐席中途登出
	 */
	public static final CloseStatus SC_SESSION_AGENTLOGOUT = new CloseStatus(4630, "坐席中途登出！");
	
	/**
	 * 坐席中途登出
	 */
	public static final CloseStatus SC_SESSION_AGENTUPDATE = new CloseStatus(4640, "坐席账号更新！");
	
	/**
	 * 数据库异常
	 */
	public static final int SC_SQL_SERROR = 1001;
	
	/**
	 * 未知的异常
	 */
	public static final int SC_UNKNOWN = 999;
	
	/**
	 * 坐席接口异常
	 * 详情见 AgentHttpStatus
	 */
	public static final AgentHttpStatus AS_SUCCESS = new AgentHttpStatus(0, "0", "成功");
	public static final AgentHttpStatus AS_ERROR = new AgentHttpStatus(999999, StringUtils.EMPTY, "坐席接口异常");
	public static final AgentHttpStatus AS_GET_EVENT_ERROR = new AgentHttpStatus(1, "000-001", "获取Agent事件的方法错误");
	public static final AgentHttpStatus AS_NO_AUTHORITY = new AgentHttpStatus(3, "000-003", "没有权限调用接口");
	public static final AgentHttpStatus AS_LOGIN_INVALID_PARAMTER = new AgentHttpStatus(100001, "100-001", "签入参数为空或者不合法");
	public static final AgentHttpStatus AS_LOGIN = new AgentHttpStatus(100002, "100-002", "座席已经登录");
	public static final AgentHttpStatus AS_LOGOUT = new AgentHttpStatus(100006, "100-006", "座席没有登录");

}
