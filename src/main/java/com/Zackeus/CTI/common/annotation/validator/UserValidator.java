package com.Zackeus.CTI.common.annotation.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import org.springframework.beans.factory.annotation.Autowired;

import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.basic.BasicValidator;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.service.UserService;

/**
 * 
 * @Title:UserValidator
 * @Description:TODO(用户校验器)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月18日 下午3:45:26
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidator.Validator.class)
public @interface UserValidator {
	
	String message() default "参数异常";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
	
	public class Validator extends BasicValidator<UserValidator, User> {
		
		@Autowired
		private UserService userService;
		
		@Override
		public void initialize(UserValidator constraintAnnotation) {
			// TODO Auto-generated method stub
		}

		@Override
		public boolean isValid(User user, ConstraintValidatorContext context) {
			List<User> workNoUsers = userService.getByAgentWorkNo(user);
			for(User workNoUser : workNoUsers) {
				if (ObjectUtils.isNotEmpty(workNoUser) && ObjectUtils.isNotEmpty(workNoUser.getAgentUser()) && StringUtils.
						isNotBlank(workNoUser.getAgentUser().getWorkno()) && !StringUtils.equals(user.getId(), workNoUser.getId())) {
					return sendErrorMsg(context, "账号：" + user.getAgentUser().getWorkno() + "，已被 " + workNoUser.getName() + " 注册！");
				}
			}

			List<User> phoneUsers = userService.getByPhone(user);
			for(User phoneUser : phoneUsers) {
				if (ObjectUtils.isNotEmpty(phoneUser) && StringUtils.isNotBlank(phoneUser.getPhone()) 
						&& !StringUtils.equals(user.getId(), phoneUser.getId())) {
					return sendErrorMsg(context, "座机：" + user.getAgentUser().getPhonenumber() + "，已被 " + phoneUser.getName() + " 占用！");
				}
			}
			return Boolean.TRUE;
		}
	}

}
