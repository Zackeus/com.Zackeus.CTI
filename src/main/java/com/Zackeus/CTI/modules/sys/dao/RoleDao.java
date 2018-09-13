package com.Zackeus.CTI.modules.sys.dao;

import java.util.List;

import com.Zackeus.CTI.common.annotation.MyBatisDao;
import com.Zackeus.CTI.common.dao.CrudDao;
import com.Zackeus.CTI.modules.sys.entity.Role;

/**
 * 
 * @Title:RoleDao
 * @Description:TODO(角色DAO接口)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月13日 下午4:23:08
 */
@MyBatisDao
public interface RoleDao extends CrudDao<Role> {
	
	public List<Role> getRoleByUser(Role role);

}
