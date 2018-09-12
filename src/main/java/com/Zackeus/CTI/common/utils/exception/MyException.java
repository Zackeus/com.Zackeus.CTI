package com.Zackeus.CTI.common.utils.exception;

/**
 * 
 * @Title:TimerException
 * @Description:TODO(排程异常类(继承运行时异常))
 * @Company:
 * @author zhou.zhang
 * @date 2018年8月23日 下午5:36:29
 */
public class MyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 错误编码
	 */
	private String errorCode;

	/**
	 * 消息是否为属性文件中的Key
	 */
	private boolean propertiesKey = true;

	/**
	 * 构造一个基本异常.
	 * 
	 * @param message
	 */
	public MyException(String message) {
		super(message);
	}

	/**
	 * 构造一个基本异常
	 * @param errorCode 错误编码
	 * @param message 信息描述
	 */
	public MyException(String errorCode, String message) {
		this(errorCode, message, true);
	}

	/**
	 * 
	 * @param errorCode 造一个基本异常.
	 * @param message
	 * @param cause
	 */
	public MyException(String errorCode, String message, Throwable cause) {
		this(errorCode, message, cause, true);
	}

	/**
	 * 构造一个基本异常. 
	 * @param errorCode 错误编码
	 * @param message 信息描述
	 * @param propertiesKey 消息是否为属性文件中的Key
	 */ 
	public MyException(String errorCode, String message, boolean propertiesKey) {
		super(message);
		this.setErrorCode(errorCode);
		this.setPropertiesKey(propertiesKey);
	}

	/**
	 * 
	 * @param errorCode
	 * @param message
	 * @param cause
	 * @param propertiesKey
	 */
	public MyException(String errorCode, String message, Throwable cause, boolean propertiesKey) {
		super(message, cause);
		this.setErrorCode(errorCode);
		this.setPropertiesKey(propertiesKey);
	}

	/**
	 * 
	 * @param message
	 * @param cause 根异常类（可以存入任何异常）
	 */
	public MyException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isPropertiesKey() {
		return propertiesKey;
	}

	public void setPropertiesKey(boolean propertiesKey) {
		this.propertiesKey = propertiesKey;
	}

}
