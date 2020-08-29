package com.web.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.oa.mapper.EmployeeMapper;
import com.web.oa.mapper.SysPermissionMapperCustom;
import com.web.oa.mapper.SysUserRoleMapper;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.EmployeeExample;
import com.web.oa.pojo.SysUserRole;
import com.web.oa.pojo.SysUserRoleExample;
import com.web.oa.service.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private SysPermissionMapperCustom permissionMapper;
	@Autowired
	private SysUserRoleMapper userRoleMapper;

	@Override
	public Employee findEmployeeByName(String name) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<Employee> list = employeeMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Employee findEmployeeManager(long id) {
		return employeeMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Employee> findUsers() {
		return employeeMapper.selectByExample(null);
	}

	@Override
	public List<EmployeeCustom> findUserAndRoleList() {
		return permissionMapper.findUserAndRoleList();
	}

	// 更新员工表职位
	@Override
	public void updateEmployeeRole(Integer roleId, String userId) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(userId);

		Employee employee = this.employeeMapper.selectByExample(example).get(0);
		employee.setRole(roleId);

		this.employeeMapper.updateByPrimaryKey(employee);
	}

	// 更新用户角色表职位
	@Override
	public void updateUserRole(String roleId, String userId) {
		SysUserRoleExample example = new SysUserRoleExample();
		SysUserRoleExample.Criteria criteria = example.createCriteria();
		criteria.andSysUserIdEqualTo(userId);

		SysUserRole userRole = userRoleMapper.selectByExample(example).get(0);
		userRole.setSysRoleId(roleId);

		userRoleMapper.updateByPrimaryKey(userRole);
	}

	// 根据员工级别查找员工信息
	@Override
	public List<Employee> findEmployeeByLevel(int level) {
		EmployeeExample example = new EmployeeExample();
		EmployeeExample.Criteria criteria = example.createCriteria();
		criteria.andRoleEqualTo(level);
		List<Employee> list = employeeMapper.selectByExample(example);

		return list;
	}

	// 查询员工信息
	@Override
	public List<Employee> findEmployeeList() {
		return this.employeeMapper.selectByExample(null);
	}

	// 添加员工
	@Override
	public void saveEmployee(Employee employee) {
		this.employeeMapper.insertSelective(employee);
	}

	// 根据id删除员工信息
	@Override
	public void deleteEmployee(Long id) {
		this.employeeMapper.deleteByPrimaryKey(id);
	}

	// 根据id查询员工信息
	@Override
	public Employee findEmployee(Long id) {
		return this.employeeMapper.selectByPrimaryKey(id);
	}

	// 根据id修改员工信息
	@Override
	public void updateEmployee(Employee employee) {
		this.employeeMapper.updateByPrimaryKeySelective(employee);
	}

	// 根据上级id查询上级
	@Override
	public Employee findEmployeeManagerByManagerId(Long managerId) {
		return this.employeeMapper.selectByPrimaryKey(managerId);
	}

}
