package com.Zackeus.CTI.modules.sys.utils;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.modules.sys.entity.Menu;
import com.Zackeus.CTI.modules.sys.entity.Principal;
import com.Zackeus.CTI.modules.sys.entity.Role;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.service.MenuService;
import com.Zackeus.CTI.modules.sys.service.RoleService;
import com.Zackeus.CTI.modules.sys.service.UserService;

/**
 * 
 * @Title:UserUtils
 * @Description:TODO(用户工具类)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月10日 上午11:50:03
 */
@Component
public class UserUtils {

	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuService menuService;
	
	public static UserUtils userUtils;
	
	@PostConstruct
	public void init() {
		userUtils = this;
	}
	
	/**
	 * 根据ID获取用户
	 * @param id
	 * @return 取不到返回null
	 */
	public static User get(String id){
		User user = userUtils.userService.get(id);
		return ObjectUtils.isEmpty(user) ? null : user;
	}
	
	/**
	 * 
	 * @Title：getByLoginName
	 * @Description: TODO(根据登录名获取用户)
	 * @see：
	 * @param loginName
	 * @return
	 */
	public static User getByLoginName(String loginName){
		User user = userUtils.userService.getByLoginName(new User(null, loginName));
		return ObjectUtils.isEmpty(user) ? null : user;
	}
	
	/**
	 * 
	 * @Title：getRoleByUser
	 * @Description: TODO(根据用户查询角色)
	 * @see：
	 * @param user
	 * @return
	 */
	public static List<Role> getRoleByUser() {
		Principal principal = getPrincipal();
		if (principal.isAdmin()) {
			return userUtils.roleService.findAllList();
		}
		return userUtils.roleService.getRoleByUser(new Role(principal));
	}
	
	/**
	 * 
	 * @Title：getMenuListByUser
	 * @Description: TODO(获取用户授权获取菜单列表)
	 * @see：
	 * @param id 菜单父级ID
	 * @return
	 */
	public static List<Menu> getMenuListByUser(String id) {
		Principal principal = getPrincipal();
		if (principal.isAdmin()) {
			return userUtils.menuService.getAllMenuList(new Menu(principal, id));
		}
		return userUtils.menuService.getMenuListByUser(new Menu(principal, id));
	}
	
	/**
	 * 
	 * @Title：getUser
	 * @Description: TODO(获取当前用户)
	 * @see：
	 * @return
	 */
	public static User getUser(){
		Principal principal = getPrincipal();
		if (principal!=null){
			User user = get(principal.getId());
			if (user != null){
				return user;
			}
			return new User();
		}
		// 如果没有登录，则返回实例化空的User对象。
		return new User();
	}
	
	/**
	 * 
	 * @Title：getSubject
	 * @Description: TODO(获取授权主要对象)
	 * @see：
	 * @return
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 
	 * @Title：getPrincipal
	 * @Description: TODO(获取当前登录者对象)
	 * @see：
	 * @return
	 */
	public static Principal getPrincipal() {
		Subject subject = SecurityUtils.getSubject();
		Principal principal = (Principal) subject.getPrincipal();
		return ObjectUtils.isEmpty(principal) ? null : principal;
	}
	
	/**
	 * 
	 * @Title：getSession
	 * @Description: TODO(获取session)
	 * @see：
	 * @return
	 */
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
		}catch (InvalidSessionException e){
			
		}
		return null;
	}
	
	/**
	 * 
	 * @Title：kickOutUser
	 * @Description: TODO((踢除指定用户)
	 * @see：
	 * @param id 用户编号
	 */
	public static void kickOutUser(String id) {
	   	// 踢出此账号在线用户
    	Collection<Session> sessions = userUtils.sessionDAO.getActiveSessions();
    	for(Session session : sessions) {
    		Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (!ObjectUtils.isEmpty(obj)) {
                Principal principal = (Principal) ((SimplePrincipalCollection) obj).getPrimaryPrincipal();
                if (id.equalsIgnoreCase(principal.getId())
                		&& !UserUtils.getSession().getId().equals(session.getId())) {
                	Logs.info("踢出用户：" + session.getId());
                	userUtils.sessionDAO.delete(session);
				}
    		}
    	}
	}
	
}
