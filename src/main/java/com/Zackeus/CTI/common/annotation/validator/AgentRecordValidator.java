package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.basic.BasicValidator;
import com.Zackeus.CTI.modules.agent.config.AgentConfig;
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
			if (StringUtils.isBlank(agentRecord.getControlSign())) {
				return sendErrorMsg(context, "{agentRecord.controlSign.NotBlank}");
			}
			
			switch (agentRecord.getControlSign()) {
			case AgentConfig.AGENT_RECORD_PLAY:
				// 录音回放
				if (StringUtils.isAllBlank(agentRecord.getCallid(), agentRecord.getRecordID())) {
					return sendErrorMsg(context, "{agentRecord.primaryKey.NotBlank}");
				}
				break;
				
			case AgentConfig.AGENT_RECORD_FORE_FAST:
			case AgentConfig.AGENT_RECORD_BACK_FAST:
				// 放音快进 快退
				if (ObjectUtils.isEmpty(agentRecord.getFastTime())) {
					return sendErrorMsg(context, "{agentRecord.fastTime.Invalid}");
				}
				break;

			default:
				break;
			}
			return Boolean.TRUE;
		}
	}

}
