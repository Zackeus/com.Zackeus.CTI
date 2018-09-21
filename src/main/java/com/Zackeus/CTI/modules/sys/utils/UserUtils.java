package com.Zackeus.CTI.modules.sys.utils;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import com.Zackeus.CTI.common.security.MySessionManager;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.common.websocket.WebSocketConfig;
import com.Zackeus.CTI.modules.sys.entity.Menu;
import com.Zackeus.CTI.modules.sys.entity.Principal;
import com.Zackeus.CTI.modules.sys.entity.Role;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.service.AgentService;
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
	private UserService userService;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private WebSocketConfig webSocketConfig;
	
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
	 * @Title：sendMessageToUser
	 * @Description: TODO(单发信息)
	 * @see：
	 */
	public static void sendMessageToUser(User user, String meg) {
		if (ObjectUtils.isNotEmpty(user) && StringUtils.isNotBlank(user.getId()) 
				&& StringUtils.isNotBlank(meg)) {
			userUtils.webSocketConfig.webSocketHandler().sendMessageToUser(user.getId(), 
					new TextMessage(meg));
		}
	}
	
	/**
	 * 
	 * @Title：sendMessageToUsers
	 * @Description: TODO(群发信息)
	 * @see：
	 * @param meg
	 */
	public static void sendMessageToUsers(String meg) {
		if (StringUtils.isNotBlank(meg)) {
			userUtils.webSocketConfig.webSocketHandler().sendMessageToUsers(new TextMessage(meg));
		}
	}
	
	/**
	 * 
	 * @Title：addOnlineUser
	 * @Description: TODO(添加在线用户)
	 * @see：
	 */
	public static void addOnlineUser() {
		Principal principal = getPrincipal();
		if (ObjectUtils.isNotEmpty(principal) && StringUtils.isNotBlank(principal.getId())) {
			MySessionManager.putSession(principal.getId());
			userUtils.agentService.addQueue(new User(principal));
		}
	}
	
	/**
	 * 
	 * @Title：addOnlineUser
	 * @Description: TODO(添加在线用户)
	 * @see：
	 * @param user
	 * @param session
	 */
	public static void addOnlineUser(User user, Session session) {
		if (ObjectUtils.isNotEmpty(user) && StringUtils.isNotBlank(user.getId())) {
			MySessionManager.putSession(user.getId(), session);
			userUtils.agentService.addQueue(user);
		}
	}
	
	/**
	 * 
	 * @Title：kickOutUser
	 * @Description: TODO(踢除指定用户)
	 * @see：
	 * @param user
	 */
	public static void kickOutUser(User user, CloseStatus closeStatus) {
		if (StringUtils.isNotBlank(user.getId())) {
			MySessionManager.deleteSession(user.getId(), closeStatus);
			userUtils.agentService.clearResourse(user);
		}
	}
}
