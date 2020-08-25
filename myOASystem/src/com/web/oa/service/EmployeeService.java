package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;

public interface EmployeeService {

	//根据员工帐号查找员工
	Employee findEmployeeByName(String name);
	
	//根据主键查找员工
	Employee findEmployeeManager(long id);
	
	List<Employee> findUsers();
	
	List<EmployeeCustom> findUserAndRoleList();
	
	void updateEmployeeRole(String roleId,String userId);
	
	List<Employee> findEmployeeByLevel(int level);

	List<Employee> findEmployeeList();

	void saveEmployee(Employee employee);

	void deleteEmployee(Long id);

	Employee findEmployee(Long id);

	void updateEmployee(Employee employee);

	Employee findEmployeeManagerByManagerId(Long managerId);
}
