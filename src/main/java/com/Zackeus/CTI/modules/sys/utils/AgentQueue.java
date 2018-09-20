package com.Zackeus.CTI.modules.sys.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * 
 * @Title:AgentQueue
 * @Description:TODO(代理队列)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月20日 下午1:15:54
 */
@Component
public class AgentQueue {
	
	/**
	 * 代理事件队列
	 */
	public final Map<String, AgentEventThread> eventThreadMap = new ConcurrentHashMap<String, AgentEventThread>();
	
	/**
	 * 用于身份验证的GUID（用于客户机服务器模式）
	 * key : userID
     * value : guid
	 */
	public final Map<String, String> guidMap = new ConcurrentHashMap<String, String>();
	
	/**
	 * elb session(For HEC)
	 */
	public final Map<String, String> elbSessionMap = new ConcurrentHashMap<String, String>();

}
