package com.web.oa.shiro;

import java.util.ArrayList;
import java.util.List;

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

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.service.EmployeeService;
import com.web.oa.service.SysService;

/**
 * 自定义Realm，进行认证与授权
 * 
 * @author 小房子
 * @date Aug 30, 2020
 *
 */
public class CustomRealm extends AuthorizingRealm {

	@Autowired
	private SysService sysService;

	@Autowired
	private EmployeeService employeeService;

	// 认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		// System.out.println("正在认证用户.....");
		String username = (String) token.getPrincipal(); // 获取用户输入的帐号

		// 根据用户名到数据库查询用户信息
		Employee user = null;
		try {
			user = employeeService.findEmployeeByName(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果用户不存在，直接返回null
		if (user == null) {
			return null; // UnknownAccountException
		}
		// 否则获取用户对应的菜单信息
		List<MenuTree> menuTree = sysService.loadMenuTree();

		// 把用户的身份信息重新封装
		ActiveUser activeUser = new ActiveUser();
		activeUser.setId(user.getId());
		activeUser.setUserid(user.getName());
		activeUser.setUsercode(user.getName());
		activeUser.setUsername(user.getName());
		activeUser.setRole(user.getRole());
		activeUser.setManagerId(user.getManagerId());
		activeUser.setMenuTree(menuTree);

		String password_db = user.getPassword(); // 数据库中的密码,密文
		// System.out.println(password_db);
		String salt = user.getSalt(); // 数据库中的盐
		// System.out.println(salt);

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(activeUser, password_db,
				ByteSource.Util.bytes(salt), "CustomRealm");
		return info;
	}

	// 授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		// 获取用户相关信息
		ActiveUser activeUser = (ActiveUser) principal.getPrimaryPrincipal();
		// 查询数据库认证用户拥有的角色和权限
		List<SysPermission> permissions = null;
		try {
			permissions = sysService.findPermissionListByUserId(activeUser.getUsername());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 用String类型的list集合存放角色和权限信息
		List<String> permisionList = new ArrayList<>();
		for (SysPermission sysPermission : permissions) {
			permisionList.add(sysPermission.getPercode());
		}

		// 返回授权信息
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permisionList);

		return info;
	}

}
