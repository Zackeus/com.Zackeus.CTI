package com.Zackeus.CTI.common.utils;

import org.springframework.util.Assert;

import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;

/**
 * 
 * @Title:AssertUtil
 * @Description:TODO(断言工具类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月21日 下午4:33:56
 */
public class AssertUtil extends Assert {
	
	private static String DEFAULT_ASSERT_MSG = "关键参数不能为空";
	
	/**
	 * 
	 * @Title：notEmpty
	 * @Description: TODO(不为空)
	 * @see：
	 * @param object
	 */
	public static void notEmpty(Object object) {
		notEmpty(object, StringUtils.EMPTY);
	}
	
	public static void notEmpty(Object object, String msg) {
		Assert.isTrue(ObjectUtils.isNotEmpty(object), StringUtils.isBlank(msg) ? DEFAULT_ASSERT_MSG : msg);
	}
	
	/**
	 * 
	 * @Title：assertAgent
	 * @Description: TODO(不为空)
	 * @see：
	 */
	public static void notEmpty(String msg, Object... objects) {
		for (Object object : objects) {
			if (ObjectUtils.isEmpty(object)) {
				throw new MyException(HttpStatus.SC_BAD_REQUEST, StringUtils.isBlank(msg) ? DEFAULT_ASSERT_MSG : msg);
			}
		}
	}
	
}
