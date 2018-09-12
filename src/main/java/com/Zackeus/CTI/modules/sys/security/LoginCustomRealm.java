package com.Zackeus.CTI.modules.sys.security;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.Zackeus.CTI.common.utils.Encodes;
import com.Zackeus.CTI.common.utils.Logs;
import com.Zackeus.CTI.common.utils.ObjectUtils;
import com.Zackeus.CTI.modules.sys.entity.Principal;
import com.Zackeus.CTI.modules.sys.entity.User;
import com.Zackeus.CTI.modules.sys.entity.UsernamePasswordToken;
import com.Zackeus.CTI.modules.sys.service.SystemService;
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
	
	 /** 
     * 登陆认证
     */
	@Override  
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
    	Logs.info("*************** 登陆认证 ******************");
    	//令牌——基于用户名和密码的令牌,把AuthenticationToken转换成UsernamePasswordToken
    	UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
    	User user = systemService.getUserByLoginName(token.getUsername());
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
		if (principal != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 添加用户权限
			info.addStringPermission("user");
			return info;
		} else {
			return null;
		}
    }
    
    /**
     * 
     * @Title：initCredentialsMatcher
     * @Description: TODO(设定密码校验的Hash算法与迭代次数,重写 shiro 密码校验规则)
     * @see：    被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次。
     * PostConstruct在构造函数之后执行,init()方法之前执行。PreDestroy（）方法在destroy()方法执行执行之后执行
     */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SystemService.HASH_ALGORITHM);
		matcher.setHashIterations(SystemService.HASH_INTERATIONS);
		setCredentialsMatcher(matcher);
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