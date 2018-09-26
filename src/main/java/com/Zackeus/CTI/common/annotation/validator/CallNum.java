package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.Zackeus.CTI.common.config.Pattern;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.basic.BasicValidator;

/**
 * 
 * @Title:CallNum
 * @Description:TODO(呼叫号码格式化)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月26日 下午4:17:20
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CallNum.Validator.class)
public @interface CallNum {
	
	String message() default "{IsValidCallNum}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class Validator extends BasicValidator<CallNum, String> {

		@Override
		public void initialize(CallNum constraintAnnotation) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return StringUtils.isNotBlank(value) && java.util.regex.Pattern.compile(Pattern.PATTEN_CALL_NUM).
					matcher(value.replaceFirst("^0+", StringUtils.EMPTY)).matches();
		}
	}

}
