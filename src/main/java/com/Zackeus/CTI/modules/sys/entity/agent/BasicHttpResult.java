package com.Zackeus.CTI.modules.sys.entity.agent;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 
 * @Title:BasicHttpResult
 * @Description:TODO(坐席接口基类)
 * @Company:
 * @author zhou.zhang
 * @date 2018年9月22日 下午2:46:33
 */
public class BasicHttpResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String retcode; 	// 错误码
	private String message; 	// 描述

	public BasicHttpResult() {
		super();
	}

	public BasicHttpResult(String message, String retcode) {
		super();
		this.message = message;
		this.retcode = retcode;
	}

	public String getRetcode() {
		return retcode;
	}

	public void setRetcode(String retcode) {
		this.retcode = retcode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
