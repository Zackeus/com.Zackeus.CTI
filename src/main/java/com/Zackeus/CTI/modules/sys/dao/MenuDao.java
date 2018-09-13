package com.Zackeus.CTI.modules.sys.dao;

import java.util.List;

import com.Zackeus.CTI.common.annotation.MyBatisDao;
import com.Zackeus.CTI.common.dao.CrudDao;
import com.Zackeus.CTI.modules.sys.entity.Menu;

/**
 * 
 * @Title:MenuDao
 * @Description:TODO(菜单DAO接口)
 * @Company:
 * @author zhou.zhang
 * @date 2018年8月14日 上午11:06:55
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {
	
	public List<Menu> getMenuList(Menu menu);
	
}
