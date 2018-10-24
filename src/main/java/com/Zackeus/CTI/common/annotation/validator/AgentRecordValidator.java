package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.basic.BasicValidator;
import com.Zackeus.CTI.modules.agent.entity.AgentRecord;

/**
 * 
 * @Title:AgentCallData
 * @Description:TODO(录音数据校验器)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年10月24日 上午9:51:29
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgentRecordValidator.Validator.class)
public @interface AgentRecordValidator {
	
	String message() default "{MissingNecessaryParameters}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	public class Validator extends BasicValidator<AgentRecordValidator, AgentRecord> {

		@Override
		public void initialize(AgentRecordValidator constraintAnnotation) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public boolean isValid(AgentRecord agentRecord, ConstraintValidatorContext context) {
			return StringUtils.isAllBlank(agentRecord.getCallid(), agentRecord.getRecordID()) ?
					sendErrorMsg(context, "{agentCallData.agentRecord.primaryKey.NotBlank}") : Boolean.TRUE;
		}
	}

}
