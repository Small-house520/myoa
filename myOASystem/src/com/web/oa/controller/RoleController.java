package com.web.oa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.SysRole;
import com.web.oa.service.EmployeeService;
import com.web.oa.service.SysService;

@Controller
public class RoleController {

	@Autowired
	private SysService sysService;

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping("/toAddRole")
	public ModelAndView toAddRole() {
		// 获取树形菜单
		List<MenuTree> allPermissions = sysService.loadMenuTree();
		// 获取菜单
		List<SysPermission> menus = sysService.findAllMenus();
		// 获取角色和权限关系
		List<SysRole> permissionList = sysService.findRolesAndPermissions();

		ModelAndView mv = new ModelAndView();
		mv.addObject("allPermissions", allPermissions);
		mv.addObject("menuTypes", menus);
		mv.addObject("roleAndPermissionsList", permissionList);
		mv.setViewName("jsp/rolelist");

		return mv;
	}

	// 给角色分配权限
	@RequestMapping("/assignRole")
	@ResponseBody
	public Map<String, String> assignRole(String roleId, String userId) {
		Map<String, String> map = new HashMap<>();
		try {
			// 更新用户角色关系表
			employeeService.updateUserRole(roleId, userId);
			// 更新用户表
			employeeService.updateEmployeeRole(Integer.parseInt(roleId), userId);
			map.put("msg", "分配权限成功");
		} catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "分配权限失败");
		}
		return map;
	}

	// 添加角色和角色权限关系
	@RequestMapping("/saveRoleAndPermissions")
	public String saveRoleAndPermissions(SysRole role, int[] permissionIds) {
		String id = this.sysService.findLastId();
		// 设置role主键，查询出最后一条记录的id，然后加一
		id = String.valueOf(Integer.parseInt(id) + 1);
		role.setId(id);

		// // 设置role主键，使用uuid
		// String uuid = UUID.randomUUID().toString();
		// role.setId(uuid);
		// 默认可用
		role.setAvailable("1");

		// 添加角色和角色权限关系
		sysService.addRoleAndPermissions(role, permissionIds);

		return "redirect:/toAddRole";
	}

	// 添加权限
	@RequestMapping("/saveSubmitPermission")
	public String saveSubmitPermission(SysPermission permission) {
		if (permission.getAvailable() == null) {
			permission.setAvailable("0");
		}
		sysService.addSysPermission(permission);
		return "redirect:/toAddRole";
	}

	// 查询所有角色及其权限关系
	@RequestMapping("/findRoles") // rest
	public ModelAndView findRoles() {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		// 查询出所有角色信息
		List<SysRole> roles = sysService.findAllRoles();
		// 查询所有菜单和权限信息
		List<MenuTree> allMenuAndPermissions = sysService.getAllMenuAndPermision();

		ModelAndView mv = new ModelAndView();
		mv.addObject("allRoles", roles);
		mv.addObject("activeUser", activeUser);
		mv.addObject("allMenuAndPermissions", allMenuAndPermissions);

		mv.setViewName("jsp/permissionlist");
		return mv;
	}

	// 加载我的权限列表
	@RequestMapping("/loadMyPermissions")
	@ResponseBody
	public List<SysPermission> loadMyPermissions(String roleId) {
		// 根据角色id查询权限信息
		List<SysPermission> list = sysService.findPermissionsByRoleId(roleId);

		for (SysPermission sysPermission : list) {
			System.out.println(sysPermission.getId() + "," + sysPermission.getType() + "\n" + sysPermission.getName()
					+ "," + sysPermission.getUrl() + "," + sysPermission.getPercode());
		}
		return list;
	}

	// 更新角色和权限关系
	@RequestMapping("/updateRoleAndPermission")
	public String updateRoleAndPermission(String roleId, int[] permissionIds) {
		sysService.updateRoleAndPermissions(roleId, permissionIds);
		return "redirect:/findRoles";
	}

	// 根据用户名查询角色和权限关系
	@RequestMapping("/viewPermissionByUser")
	@ResponseBody
	public SysRole viewPermissionByUser(String userName) {
		SysRole sysRole = sysService.findRolesAndPermissionsByUserId(userName);

		System.out.println(sysRole.getName() + "," + sysRole.getPermissionList());
		return sysRole;
	}

	// 查询上级列表
	@RequestMapping("/findNextManager")
	@ResponseBody
	public List<Employee> findNextManager(int level) {
		if (level < 3) {
			level++; // 加一，表示上一个级别
		} else if (level == 4 || level == 3) {
			level = 3;
		}
		// 查询上级列表
		List<Employee> list = employeeService.findEmployeeByLevel(level);
		return list;

	}

	// 删除角色
	@RequestMapping("/roledel")
	public String roledel(String id) {
		this.sysService.deleteRole(id);
		return "redirect:/findRoles";
	}
}
