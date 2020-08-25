package com.web.oa.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.service.EmployeeService;

public class ManagerTaskHandler implements TaskListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {

		// spring容器
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		// HttpServletRequest request = ((ServletRequestAttributes)
		// RequestContextHolder.getRequestAttributes()) .getRequest();

		/** 从新查询当前用户，再获取当前用户对应的领导 */
		// Employee emp = (Employee)
		// request.getSession().getAttribute(Constants.GLOBLE_USER_SESSION);
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

		EmployeeService employeeService = (EmployeeService) context.getBean("employeeService");
		Employee manager = employeeService.findEmployeeManager(activeUser.getManagerId());
		// 设置个人任务的办理人
		delegateTask.setAssignee(manager.getName());

	}

}
