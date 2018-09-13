package com.Zackeus.CTI.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.modules.sys.dao.MenuDao;
import com.Zackeus.CTI.modules.sys.entity.Menu;

/**
 * 
 * @Title:MenuService
 * @Description:TODO(菜单Service)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月15日 下午12:38:26
 */
@Service("menuService")
public class MenuService extends CrudService<MenuDao, Menu> {
	
	@Autowired
	private MenuDao menuDao;
	
	/**
	 * 
	 * @Title：findAllList
	 * @Description: TODO(获取全部菜单列表（父子菜单不嵌套）)
	 * @see：
	 * @param menu
	 * @return
	 */
	@Cacheable(value = {"sysMenuCache"}, keyGenerator = "cacheKeyGenerator")
	public List<Menu> findAllList() {
		return super.findAllList();
	}
	
	/**
	 * 
	 * @Title：getMenuList
	 * @Description: TODO(获取用户授权获取菜单列表)
	 * @see：
	 * @param menu
	 * @return
	 */
	@Cacheable(value = {"sysMenuCache"}, keyGenerator = "cacheKeyGenerator")
	public List<Menu> getMenuList(Menu menu) {
		return menuDao.getMenuList(menu);
	}
	
}
