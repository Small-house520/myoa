package com.web.oa.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Employee;
import com.web.oa.pojo.EmployeeCustom;
import com.web.oa.pojo.SysRole;
import com.web.oa.service.EmployeeService;
import com.web.oa.service.SysService;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;

@Controller
public class UserController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private SysService sysService;

	// 生成验证码
	@RequestMapping("/checkcode")
	public void checkcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 定义图形验证码的长、宽、验证码字符数、干扰线宽度
		ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(112, 40, 4, 3);
		// 得到code
		String code = captcha.getCode();
		System.out.println(code);
		// 将验证码存入session
		request.getSession().setAttribute("checkCode_session", code);
		// 图形验证码写出，可以写出到文件，也可以写出到流
		// captcha.write("d:/shear.png");
		// 输出流
		ServletOutputStream outputStream = response.getOutputStream();
		// 读写输出流
		captcha.write(outputStream);
		// 关闭输出流
		outputStream.close();
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request, Model model) {

		// 获取cookie
		Cookie[] cookies = request.getCookies();
		// 如果cookie不为空
		if (cookies != null && cookies.length > 0) {
			String username = null;
			String password = null;
			// 取出cookie中存储的用户名和密码
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("username")) {
					username = cookie.getValue();
				}
				if (cookie.getName().equals("password")) {
					password = cookie.getValue();
				}
			}
			// 用map接收用户名和密码
			Map<String, String> map = new HashMap<>();
			map.put("username", username);
			map.put("password", password);
			// 把map设置到model
			model.addAllAttributes(map);
		}

		// 如果登陆失败从request中获取认证异常信息，shiroLoginFailure就是shiro异常类的全限定名
		String exceptionName = (String) request.getAttribute("shiroLoginFailure");
		// 根据shiro返回的异常类路径判断，抛出指定异常信息
		if (exceptionName != null) {
			if (UnknownAccountException.class.getName().equals(exceptionName)) {
				// 最终会抛给异常处理器
				model.addAttribute("errorMsg", "账号不存在！");
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionName)) {
				model.addAttribute("errorMsg", "用户名或密码不正确！");
			} else if ("randomCodeError".equals(exceptionName)) {
				model.addAttribute("errorMsg", "验证码不正确！");
			} else {
				model.addAttribute("errorMsg", "未知错误！");
			}
		}
		// 此方法不处理登陆成功（认证成功），shiro认证成功会自动跳转到上一个请求路径
		// 登陆失败还到login页面
		return "login";
	}

	/*
	 * SELECT e1.*,e2.name FROM employee e1 INNER JOIN employee e2 WHERE
	 * e1.manager_id=e2.id;
	 */
	@RequestMapping("/findUserList")
	public ModelAndView findUserList(String userId) {
		ModelAndView mv = new ModelAndView();
		List<SysRole> allRoles = sysService.findAllRoles();
		List<EmployeeCustom> list = employeeService.findUserAndRoleList();

		mv.addObject("userList", list);
		mv.addObject("allRoles", allRoles);

		mv.setViewName("jsp/userlist");
		return mv;
	}

	// 跳转到添加员工页面
	@RequestMapping("/employeeadd")
	public String employeeadd(Model model) {
		List<Employee> employees = this.employeeService.findEmployeeList();
		model.addAttribute("employees", employees);

		return "jsp/employeeadd";
	}

	// 添加员工
	@RequestMapping("/saveEmployee")
	public String saveEmployee(Employee employee) {
		// 对密码进行md5加密处理
		String salt = "eteokues";
		Md5Hash md5Hash = new Md5Hash(employee.getPassword(), salt, 2);
		employee.setPassword(md5Hash.toString());
		employee.setSalt(salt);

		this.employeeService.saveEmployee(employee);
		return "redirect:/employeelist";
	}

	// 删除员工信息
	@RequestMapping("/employeedelete")
	public String employeedelete(Long id) {
		this.employeeService.deleteEmployee(id);
		return "redirect:/employeelist";
	}

	// 跳转到修改员工页面
	@RequestMapping("/employeeedit")
	public String employeeedit(Long id, Model model, HttpSession session) {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();

		// 获取要修改员工的信息
		if (id == 0) {
			id = activeUser.getId();
		}
		Employee employee = this.employeeService.findEmployee(id);

		List<Employee> employees = this.employeeService.findEmployeeList();

		// 把查询结果传递给model
		Map<String, Object> map = new HashMap<>();
		map.put("employee", employee);
		map.put("employees", employees);
		model.addAllAttributes(map);

		return "jsp/employeeedit";
	}

	// 修改员工信息
	@RequestMapping("/updateEmployee")
	public String updateEmployee(Employee employee) {
		this.employeeService.updateEmployee(employee);
		return "redirect:/employeelist";
	}

	// 查询员工信息
	@RequestMapping("/employeelist")
	public String findEmployeeList(Model model) {
		List<Employee> employees = this.employeeService.findEmployeeList();
		model.addAttribute("employees", employees);
		return "jsp/employeelist";
	}

}
