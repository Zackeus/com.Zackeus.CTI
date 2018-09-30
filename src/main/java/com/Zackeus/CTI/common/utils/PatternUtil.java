package com.Zackeus.CTI.common.utils;

import java.util.regex.Pattern;

/**
 * 
 * @Title:PatternUtil
 * @Description:TODO(正则工具类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月29日 下午4:08:26
 */
public class PatternUtil {
	
	/*
	 * 号码正则
	 */
	public static final String PATTEN_CALL_NUM = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
	
	/**
	 * URL正则
	 */
	public static final String PATTEN_URL = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$";
	
	/**
	 * 
	 * @Title：name
	 * @Description: TODO(正则校验)
	 * @see：
	 * @param pattern 正则字符
	 * @param value	校验字符
	 * @return
	 */
	public static Boolean check(String pattern, String value) {
		return Pattern.compile(pattern).matcher(value).matches();
	}
	
	public static void main(String[] args) {
		System.out.println(check(PATTEN_URL, "http://10.5.133.244:8008/com.Zackeus.CTI/sys/demo/receive"));
	}
}
