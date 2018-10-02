package com.Zackeus.CTI.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.httpClient.HttpStatus;
import com.Zackeus.CTI.modules.sys.dao.UserDao;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

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
	 * @Description: TODO(根据坐席工号查询用户(剔除待查询账号))
	 * @see：
	 * @param user
	 * @return
	 */
	public List<User> getByAgentWorkNo(User user) {
		return dao.getByAgentWorkNo(user);
	}
	
	/**
	 * 
	 * @Title：getByPhone
	 * @Description: TODO(根据座机号查询用户(剔除待查询账号))
	 * @see：
	 * @param user
	 * @return
	 */
	public List<User> getByPhone(User user) {
		return dao.getByPhone(user);
	}

	/**
	 * 更新用户
	 */
	@Override
	public void save(User user) {
		if (ObjectUtils.isEmpty(dao.getAgentUser(user))) {
			// 执行插入操作
			user.setIsNewRecord(Boolean.TRUE);
			user.preInsert();
			dao.insert(user);
		} else {
			// 更新操作
			user.preUpdate();
			dao.update(user);
		}
		dao.updatePhone(user);
		// 踢出更新账户
		UserUtils.kickOutUser(user, HttpStatus.SC_SESSION_AGENTUPDATE);
	}
	
	/**
	 * 
	 * @Title：cancelUser
	 * @Description: TODO(注销用户)
	 * @see：
	 * @param user
	 * @param id
	 */
	public void cancelUser(User user) {
		dao.delete(user);
	}
	
}
