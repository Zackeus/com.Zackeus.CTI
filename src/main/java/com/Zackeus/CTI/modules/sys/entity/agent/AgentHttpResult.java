package com.Zackeus.CTI.modules.sys.entity.agent;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @Title:AgentHttpResult
 * @Description:TODO(坐席接口返回参数)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月21日 上午11:47:45
 */
public class AgentHttpResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message; 	// 描述
	private String retcode; 	// 错误码
	private Result result; 		// 查询成功后，返回结果信息的对象

	public AgentHttpResult() {
		super();
	}

	public AgentHttpResult(String message, String retcode, Result result) {
		super();
		this.message = message;
		this.retcode = retcode;
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
