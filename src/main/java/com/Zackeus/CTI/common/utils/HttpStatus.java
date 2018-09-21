package com.Zackeus.CTI.common.utils;

import org.springframework.web.socket.CloseStatus;

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
	public static final CloseStatus SC_SESSION_EXPRIES = new CloseStatus(4700, "会话失效！");
	
	/**
	 * 数据库异常
	 */
	public static final int SC_SQL_SERROR = 1001;
	
	/**
	 * 未知的异常
	 */
	public static final int SC_UNKNOWN = 999;
	
	/**
	 * 自定义异常
	 */
	public static final int SC_CUSTOM_A = -100;
	public static final int SC_CUSTOM_B = -200;
	public static final int SC_CUSTOM_C = -300;
	
	/**
	 * 坐席接口异常
	 * 3000000范围状态码为坐席接口状态码
	 * 详情见 AgentResultStatus
	 */
	public static final Integer AGENT_ERROR = 3999999;

}
