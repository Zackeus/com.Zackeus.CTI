package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Hour.Validator.class)
public @interface Hour {
	
	String message() default "不是小时格式";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	public class Validator implements ConstraintValidator<Hour, String> {

		@Override
		public void initialize(Hour constraintAnnotation) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			return value.matches("(0\\d{1}|1\\d{1}|2[0-4])");
		}
		
	}
	
}
