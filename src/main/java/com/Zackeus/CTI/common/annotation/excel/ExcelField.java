package com.Zackeus.CTI.common.annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @Title:ExcelField
 * @Description:TODO(excel导出注解类)
 * @Company:
 * @author zhou.zhang
 * @date 2018年7月17日 上午10:53:02
 */
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {
	
	// 导出字段在excel中的名字
	String title();
}
