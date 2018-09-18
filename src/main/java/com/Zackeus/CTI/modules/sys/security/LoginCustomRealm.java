package com.Zackeus.CTI.modules.sys.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.Zackeus.CTI.common.utils.Encodes;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.common.utils.StringUtils;
import com.Zackeus.CTI.modules.sys.entity.Principal;
import com.Zackeus.CTI.modules.sys.entity.Role;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.entity.UsernamePasswordToken;
import com.Zackeus.CTI.modules.sys.service.SystemService;
import com.Zackeus.CTI.modules.sys.service.UserService;
import com.Zackeus.CTI.modules.sys.utils.UserUtils;

/**
 * 
 * @Title:CustomRealm
 * @Description:TODO(登录认证校验器)
 * @Company: 
 * @author zhou.zhang
 * @date 2018年8月10日 上午10:49:28
 */
public class LoginCustomRealm extends AuthorizingRealm {
	
	@Autowired
	SystemService systemService;
	
	@Autowired
	UserService userService;
	
	 /** 
     * 登陆认证
     */
	@Override  
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
    	Logs.info("*************** 登陆认证 ******************");
    	//令牌——基于用户名和密码的令牌,把AuthenticationToken转换成UsernamePasswordToken
    	UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    	User user = systemService.getUserByLoginName(token.getUsername());
    	
    	if (ObjectUtils.isEmpty(user.getAgentUser()) || StringUtils.isBlank(user.getAgentUser().getWorkno())) {
    		throw new AuthenticationException("msg:此账号未注册坐席账号");
		}
    	
    	if (StringUtils.isBlank(user.getAgentUser().getPhonenumber())) {
    		throw new AuthenticationException("msg:此账号未绑定座机号");
		}
    	
    	if (ObjectUtils.isNotEmpty(userService.getByAgentWorkNo(user))) {
    		throw new AuthenticationException("msg:坐席工号:" + user.getAgentUser().getWorkno() + "已被多个用户注册，请联系管理员");
		}
    	
    	if (ObjectUtils.isNotEmpty(userService.getByPhone(user))) {
    		throw new AuthenticationException("msg:座机号:" + user.getAgentUser().getPhonenumber() + "已被多个用户占用，请联系管理员");
		}
    	
    	if (!ObjectUtils.isEmpty(user)) {
        	byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
        	return new SimpleAuthenticationInfo(new Principal(user, false), 
        			user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
    	} 
    	return null;
    } 
    
	/**
	 * 获取权限授权信息，如果缓存中存在，则直接从缓存中获取，否则就重新获取， 登录成功后调用
	 */
	protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
		return super.getAuthorizationInfo(principals);
	}
    
    /**
     * 获取授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	Logs.info("获取授权----------------");
    	
    	Principal principal = UserUtils.getPrincipal();
		if (ObjectUtils.isNotEmpty(principal)) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			for (Role role : UserUtils.getRoleByUser()) {
				info.addRole(role.getEnName());
			}
			return info;
		} else {
			return null;
		}
    }
    
	/**
	 * 
	 * @Title：clearCached
	 * @Description: TODO(清除缓存)
	 * @see：
	 */
    public void clearCached() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();  
        super.clearCache(principals);  
    }  
}