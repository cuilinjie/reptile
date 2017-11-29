package com.clj.reptilehouse.system.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.clj.reptilehouse.system.entity.SysPrivilegeItem;
import com.clj.reptilehouse.system.entity.SysUser;
import com.clj.reptilehouse.system.service.UserService;

public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeItemService privilegeItemService;

	/**
	 * 认证回调函数, 登录时调用.
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		SysUser user = userService.findUserByLoginName(token.getUsername());
		if (user != null) {
			return new SimpleAuthenticationInfo(user.getLoginName(), user.getLoginPwd(), getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String loginName = (String) principals.fromRealm(getName()).iterator().next();
		SysUser user = userService.findUserByLoginName(loginName);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<SysPrivilegeItem> listpriv =null;
			if("devadmin".equals(user.getLoginName())){
				listpriv= privilegeItemService.getAllPriv();
			}else{
				listpriv= privilegeItemService.findUserAllPriv(user.getId());
			}
			Collection<String> perms = new ArrayList<String>();  
			for (SysPrivilegeItem privilege : listpriv) {
				perms.add(privilege.getCode());
			}
			info.addStringPermissions(perms);
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}

}
