package com.Zackeus.CTI.modules.agent.entity;


/**
 * 
 * @Title:AgentHttpResult
 * @Description:TODO(坐席接口返回参数)
 * @Company:
 * @author zhou.zhang
 * @param <T>
 * @date 2018年9月21日 上午11:47:45
 */
public class AgentHttpResult<T> extends BasicHttpResult {

	private static final long serialVersionUID = 1L;

	private T result; 		// 查询成功后，返回结果信息的对象

	public AgentHttpResult() {
		super();
	}
	
	public AgentHttpResult(String message, String retcode) {
		super(message, retcode);
	}

	public AgentHttpResult(T result) {
		super();
		this.result = result;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
}
