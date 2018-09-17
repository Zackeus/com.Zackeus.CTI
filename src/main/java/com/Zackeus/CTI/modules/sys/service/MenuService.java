package com.Zackeus.CTI.modules.sys.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.Zackeus.CTI.common.service.CrudService;
import com.Zackeus.CTI.common.utils.ObjectUtils;
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
	
	/**
	 * 
	 * @Title：getMneuByParentId
	 * @Description: TODO(根据Id查询菜单)
	 * @see：
	 * @param parentid
	 * @return 
	 */
	@Cacheable(value = {"sysMenuCache"}, keyGenerator = "cacheKeyGenerator")
	@Override
	public Menu get(String id) {
		return super.get(id);
	}

	/**
	 * 
	 * @Title：getMaxSortById
	 * @Description: TODO(根据 id 查询子菜单最大最大排序值)
	 * @see：
	 * @param id
	 * @return
	 */
	@Cacheable(value = {"sysMenuCache"}, keyGenerator = "cacheKeyGenerator")
	public Integer getMaxSortById(String id) {
		return dao.getMaxSortById(id);
	}
	
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
	 * @Title：getAllMenuList
	 * @Description: TODO(根据父级标识获取全部子菜单列表（父子菜单嵌套）)
	 * @see：此方法不能使用缓存，因开启了懒加载模式，开启缓存可能会使嵌套查询不执行
	 * @param menu
	 * @return
	 */
	public List<Menu> getAllMenuList(Menu menu) {
		return dao.getAllMenuList(menu);
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
	public List<Menu> getMenuListByUser(Menu menu) {
		return dao.getMenuListByUser(menu);
	}
	
	/**
	 * 
	 * @Title：save
	 * @Description: TODO(菜单添加和更新)
	 * @see：
	 * @param menu
	 * @return
	 */
	@CacheEvict(value = {"sysMenuCache"}, allEntries = true, beforeInvocation = true)
	@Override
	public void save(Menu entity) {
		super.save(entity);
	}
	
	/**
	 * 
	 * @Title：delete
	 * @Description: TODO(删除菜单)
	 * @see：
	 * @param menu
	 * @return
	 */
	@CacheEvict(value = {"sysMenuCache"}, allEntries = true, beforeInvocation = true)
	@Override
	public void delete(Menu entity) {
		Menu menu = get(entity.getId());
		List<Menu> childrens = getAllMenuList(menu);
		super.delete(entity);
		
		if (ObjectUtils.isNotEmpty(childrens)) {
			for (Menu childMenu : childrens) {
	            if (ObjectUtils.isEmpty(childMenu.getChildren())) {
	            	super.delete(childMenu);
	            } else {
	                //递归删除节点
	                this.delete(childMenu);
	            }
	        }
		} 
	}
	
}
