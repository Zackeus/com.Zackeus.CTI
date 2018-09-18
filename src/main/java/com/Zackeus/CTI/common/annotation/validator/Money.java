package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 
 * @Title:Money
 * @Description:TODO(金额校验类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月28日 下午2:48:44
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Money.Validator.class)
public @interface Money {

	String message() default "不是金额形式";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class Validator implements ConstraintValidator<Money, Double> {
		// 表示金额的正则表达式
		private String moneyReg = "^\\d+(\\.\\d{1,2})?$";
		private Pattern moneyPattern = Pattern.compile(moneyReg);

		@Override
		public void initialize(Money constraintAnnotation) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public boolean isValid(Double value, ConstraintValidatorContext context) {
			if (value == null)
				return true;
			return moneyPattern.matcher(value.toString()).matches();
		}

	}

}
