package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.SpringContextUtil;
import com.Zackeus.CTI.common.utils.StringUtils;

/**
 * 
 * @Title:DataVerificat
 * @Description:TODO(数据校验器 操作数据据库前校验数据是否存在 要相应的 service 
 * 实现 CrudService 的 get(String id) 方法,实现此方法最好整合缓存机制)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月30日 下午2:28:52
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DataVerificat.Validator.class)
public @interface DataVerificat {
	
	String message() default "{IsExistData}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	Class<? extends CrudService<?, ?>> service();
	
	String methodName() default "";
	
	public class Validator implements ConstraintValidator<DataVerificat, String> {
		
		// 数据校验 Service
		private Class<? extends CrudService<?, ?>> service;
		// 数据校验所用的方法名，如果不传此参数，则使用默认即 CrudService 的 get(String id) 方法
		private String methodName;

		@Override
		public void initialize(DataVerificat constraintAnnotation) {
			service = constraintAnnotation.service();
			methodName = constraintAnnotation.methodName();
		}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			if (StringUtils.isBlank(value)) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("{IsNotBlank}").addConstraintViolation();
				return Boolean.FALSE;
			}
			
			if (StringUtils.isNotBlank(methodName)) {
				try {
					Method method = service.getDeclaredMethod(methodName, String.class);
					return !ObjectUtils.isEmpty(method.invoke(SpringContextUtil.getBeanByName(service), value));
				} catch (Exception e) {
					Logs.error("数据校验器异常：" + e.getMessage());
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("数据校验器异常: " + e.getMessage()).addConstraintViolation();
					return Boolean.FALSE;
				}
			}
			
			return !ObjectUtils.isEmpty(SpringContextUtil.getBeanByName(service).get(value));
		}
	}
}
