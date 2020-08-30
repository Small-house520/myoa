package com.web.oa.utils;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.service.EmployeeService;

/**
 * 自定义监听器，分配待办人
 * 
 * @author 小房子
 * @date Aug 30, 2020
 *
 */
public class CustomerLisnter implements TaskListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask deletegate) {

		/*
		 * EmployeeService e = new EmployeeServiceImpl();
		 * e.findEmployeeManagerByManagerId(managerId);
		 */

		// 调用 EmployeeService 查询出 当前待办人的上司是谁?

		// 拿到spring 容器
		WebApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();

		// 通过应用上下文获取EmployeeService
		EmployeeService emService = (EmployeeService) applicationContext.getBean("employeeService");

		// 获取request对象
		// HttpServletRequest request = ((ServletRequestAttributes)
		// RequestContextHolder.getRequestAttributes())
		// .getRequest();
		// 获取session
		// HttpSession session = request.getSession();

		// 取出当前用户对象
		// Employee employee = (Employee)
		// session.getAttribute(Constants.GLOBLE_USER_SESSION);
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

		// 调用服务层中方法根据managerId 查询出当前待办人的上级
		Employee manager = emService.findEmployeeManagerByManagerId(activeUser.getManagerId());

		// 设置待办人
		deletegate.setAssignee(manager.getName());

	}

}
