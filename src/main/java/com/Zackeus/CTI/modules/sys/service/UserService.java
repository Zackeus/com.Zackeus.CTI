package com.Zackeus.CTI.modules.sys.service;

import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.utils.exception.MyException;
import com.Zackeus.CTI.modules.sys.dao.UserDao;
import com.Zackeus.CTI.modules.sys.entity.User;

/**
 * 
 * @Title:UserService
 * @Description:TODO(用户Service)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年9月4日 下午4:06:33
 */
@Service("userService")
public class UserService extends CrudService<UserDao, User> {
	
	/**
	 * 
	 * @Title：getByLoginName
	 * @Description: TODO(根据登录名称查询用户)
	 * @see：
	 * @param user
	 * @return
	 */
	public User getByLoginName(User user) {
		return dao.getByLoginName(user);
	}
	
	/**
	 * 
	 * @Title：getByAgentWorkNo
	 * @Description: TODO(根据坐席工号查询用户)
	 * @see：
	 * @param user
	 * @return
	 */
	public User getByAgentWorkNo(User user) {
		return dao.getByAgentWorkNo(user);
	}

	/**
	 * 更新用户
	 */
	@Override
	public void save(User user) {
		User agentUser = getByAgentWorkNo(user);
		if (ObjectUtils.isNotEmpty(agentUser.getAgentUser()) && 
				StringUtils.isNotBlank(agentUser.getAgentUser().getWorkno())) {
			throw new MyException("账号：" + user.getAgentUser().getWorkno() + "，已被 " + agentUser.getName() + " 注册！");
		}
	}
	
}
