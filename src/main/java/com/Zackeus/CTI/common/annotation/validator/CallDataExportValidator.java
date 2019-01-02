package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.Zackeus.CTI.common.utils.DateUtils;
import com.Zackeus.CTI.common.utils.basic.BasicValidator;
import com.Zackeus.CTI.modules.agent.entity.CallDataExport;

/**
 * 
 * @Title:AgentCallData
 * @Description:TODO(通话报表校验器)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年10月24日 上午9:51:29
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CallDataExportValidator.Validator.class)
public @interface CallDataExportValidator {
	
	int overTime() default 31;
	
	String message() default "{MissingNecessaryParameters}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class Validator extends BasicValidator<CallDataExportValidator, CallDataExport> {
		
		private Integer overTime;
		
		@Override
		public void initialize(CallDataExportValidator constraintAnnotation) {
			overTime = constraintAnnotation.overTime();
		}
		
		@Override
		public boolean isValid(CallDataExport callDataExport, ConstraintValidatorContext context) {
			if (DateUtils.getDistanceOfTwoDate(callDataExport.getStartDate(), callDataExport.getEndDate()) > overTime)
				return sendErrorMsg(context, "{callDataExport.overtime}");
			return Boolean.TRUE;
		}
	}

}

