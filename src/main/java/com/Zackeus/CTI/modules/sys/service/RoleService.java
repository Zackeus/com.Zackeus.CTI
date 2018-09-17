package com.Zackeus.CTI.modules.sys.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.modules.sys.dao.RoleDao;
import com.Zackeus.CTI.modules.sys.entity.Role;

/**
 * 
 * @Title:RoleService
 * @Description:TODO(角色Service)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月15日 下午1:10:15
 */
@Service("roleService")
public class RoleService extends CrudService<RoleDao, Role> {

	/**
	 * 获取全部角色列表
	 */
	@Cacheable(value = {"authorizationCache"}, keyGenerator = "cacheKeyGenerator")
	public List<Role> findAllList() {
		return super.findAllList();
	}
	
	/**
	 * 
	 * @Title：getRoleByUser
	 * @Description: TODO(根据用户查询角色)
	 * @see：
	 * @param role
	 * @return
	 */
	@Cacheable(value = {"authorizationCache"}, keyGenerator = "cacheKeyGenerator")
	public List<Role> getRoleByUser(Role role) {
		return dao.getRoleByUser(role);
	}
	
}
