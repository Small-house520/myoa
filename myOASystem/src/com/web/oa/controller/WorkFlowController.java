package com.web.oa.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.BaoxiaoBill;
import com.web.oa.pojo.Leavebill;
import com.web.oa.service.BaoxiaoService;
import com.web.oa.service.LeaveBillService;
import com.web.oa.service.WorkFlowService;

@Controller
public class WorkFlowController {

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	private LeaveBillService leaveBillService;

	@Autowired
	private BaoxiaoService baoxiaoService;

	// 部署流程
	@RequestMapping("/deployProcess")
	public String deployProcess(String processName, MultipartFile fileName) {
		try {
			// 传入一个文件输入流和部署名字
			workFlowService.saveNewDeploye(fileName.getInputStream(), processName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/processDefinitionList";
	}

	// 保存请假信息，开启请假流程
	@RequestMapping("/saveStartLeave")
	public String saveStartLeave(Leavebill leaveBill, RedirectAttributes redirectAttributes) {

		/**
		 * 1.将请假业务信息插入到 leavBill表中
		 * 
		 * 2.启动当前流程
		 * 
		 */
		leaveBill.setLeavedate(new Date());

		/**
		 * 1 表示当前流程正在运行
		 * 
		 * 2 表示当前流程已经全部结束
		 */
		leaveBill.setState(1);

		// 获取session中的employee
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		// Employee employee = (Employee)
		// session.getAttribute(Constants.GLOBLE_USER_SESSION);

		leaveBill.setUserId(activeUser.getId());

		// 保存请假单
		this.leaveBillService.saveLeaveBill(leaveBill);

		// 启动流程(待办人)
		// this.workFlowService.startProcess(employee.getName());
		// this.workFlowService.startProcess2(leaveBill.getId(),
		// activeUser.getUsername());
		this.workFlowService.startProcess2(leaveBill.getId(), activeUser.getUsername());

		// 这种方法相当于在重定向链接地址上追加传递的参数
		// redirectAttributes.addAttribute("flag", 1);

		// 这种方法是隐藏了参数，链接地址上不直接暴露，
		// 用(@ModelAttribute(value = "prama")String prama)的方式获取参数。
		redirectAttributes.addFlashAttribute("flag", 1);
		return "redirect:/myTaskList";
	}

	@RequestMapping("/saveStartBaoxiao")
	public String saveStartBaoxiao(BaoxiaoBill baoxiaoBill, RedirectAttributes redirectAttributes) {
		// 设置当前时间
		baoxiaoBill.setCreatdate(new Date());
		// 设置申请人ID
		// Employee employee = (Employee)
		// session.getAttribute(Constants.GLOBLE_USER_SESSION);
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		baoxiaoBill.setUserId(activeUser.getId());
		// 更新状态从0变成1（初始录入-->审核中）
		baoxiaoBill.setState(1);
		baoxiaoService.saveBaoxiao(baoxiaoBill);

		workFlowService.saveStartProcess(baoxiaoBill.getId(), activeUser.getUsername());

		// 这种方法相当于在重定向链接地址上追加传递的参数
		// redirectAttributes.addAttribute("flag", 1);

		// 这种方法是隐藏了参数，链接地址上不直接暴露，
		// 用(@ModelAttribute(value = "prama")String prama)的方式获取参数。
		redirectAttributes.addFlashAttribute("flag", 2);
		return "redirect:/myTaskList";
	}

	// 根据待办人名称查询请假任务，并跳转到前台显示
	@RequestMapping("/myTaskList")
	// @ModelAttribute("")接收重定向携带的隐藏参数
	public ModelAndView getTaskList(@ModelAttribute("flag") Integer flag, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		// 获取session中的employee
		// Employee employee = (Employee)
		// session.getAttribute(Constants.GLOBLE_USER_SESSION);
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		// 根据待办人查询任务
		List<Task> list = this.workFlowService.findTaskListByName(activeUser.getUsername(), flag);

		// 将查询到的list 保存model域中
		mv.addObject("taskList", list);
		mv.addObject("flag", flag);

		mv.setViewName("jsp/workflow_task");
		return mv;
	}

	// 根据taskId查询出 请假单信息 和批注列表信息
	@RequestMapping("/viewTaskForm")
	public String findLeaveBill(String taskId, int flag, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		String uri = "";
		if (flag == 1) {
			uri = "jsp/approve_leave";
			// 获取请假单信息
			Leavebill leavebill = this.workFlowService.findLeaveBillListByTaskId(taskId);
			map.put("bill", leavebill);
		} else {
			uri = "jsp/approve_baoxiao";
			// 获取报销单信息
			BaoxiaoBill baoxiaobill = this.workFlowService.findBaoxiaoBillListByTaskId(taskId);
			map.put("baoxiaoBill", baoxiaobill);

		}
		// 获取对应身份的审核功能信息
		List<String> outcomeList = this.workFlowService.findOutComeListByTaskId(taskId);
		if (outcomeList != null && (outcomeList.get(0).contains("请假") || outcomeList.get(0).contains("报销"))) {
			outcomeList.clear();
			outcomeList.add("提交申请");
		}
		map.put("outcomeList", outcomeList);

		// 获取批注信息
		List<Comment> comments = this.workFlowService.findCommentListByTaskId(taskId);

		// 用map接收获取到的信息
		map.put("commentList", comments);
		map.put("taskId", taskId);
		// 把设置到model
		model.addAllAttributes(map);

		return uri;
	}

	// 办理任务
	@RequestMapping("/submitTask")
	public String submitTask(long id, String taskId, String comment, String outcome, Integer flag, Model model) {
		// 获取员工信息
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		String username = activeUser.getUsername();
		// 添加批注，且流程需要往前面推进
		this.workFlowService.saveSubmitTask(id, taskId, comment, outcome, username, flag);

		model.addAttribute("flag", flag);
		String uri = "forward:/myLeaveBill";
		if (flag == 2) {
			uri = "forward:/myBaoxiaoBill";
		}
		return uri;
	}

	/**
	 * 查看当前流程图（查看当前活动节点，并使用红色的框标注）
	 */
	@RequestMapping("/viewCurrentImage")
	public String viewCurrentImage(String taskId, ModelMap model) {
		/** 一：查看流程图 */
		// 1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);

		model.addAttribute("deploymentId", pd.getDeploymentId());
		model.addAttribute("imageName", pd.getDiagramResourceName());
		/** 二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中 */
		Map<String, Object> map = workFlowService.findCoordingByTask(taskId);

		model.addAttribute("acs", map);
		return "jsp/viewimage";
	}

	/**
	 * 查看流程图
	 * 
	 * @throws Exception
	 */
	@RequestMapping("/viewImage")
	public String viewImage(String deploymentId, String imageName, HttpServletResponse response) throws Exception {

		// 2：获取资源文件表（act_ge_bytearray）中资源图片输入流InputStream
		InputStream in = workFlowService.findImageInputStream(deploymentId, imageName);
		// 3：从response对象获取输出流
		OutputStream out = response.getOutputStream();
		// 4：将输入流中的数据读取出来，写到输出流中
		for (int b = -1; (b = in.read()) != -1;) {
			out.write(b);
		}
		out.close();
		in.close();
		return null;
	}

	// 查询流程信息
	@RequestMapping("/processDefinitionList")
	public ModelAndView processDefinitionList() {
		ModelAndView mv = new ModelAndView();

		// 1:查询部署对象信息，对应表（act_re_deployment）
		List<Deployment> depList = workFlowService.findDeploymentList();
		// 2:查询流程定义的信息，对应表（act_re_procdef）
		List<ProcessDefinition> pdList = workFlowService.findProcessDefinitionList();
		// 放置到上下文对象中
		mv.addObject("depList", depList);
		mv.addObject("pdList", pdList);

		mv.setViewName("jsp/workflow_list");
		return mv;
	}

	// 查看历史的批注信息
	@RequestMapping("/viewHisComment")
	public String viewHisComment(long id, int flag, ModelMap model) {

		List<Comment> commentList = null;
		String uri = "";
		if (flag == 1) {
			uri = "jsp/leave_commentlist";
			// 1：使用请假单ID，查询请假单对象
			Leavebill bill = leaveBillService.findLeaveBillById(id);
			model.addAttribute("leaveBill", bill);
			// 2：使用请假单ID，查询历史的批注信息
			commentList = workFlowService.findCommentByLeaveBillId(id);
		} else if (flag == 2) {
			uri = "jsp/baoxiao_commentlist";
			// 1：使用报销单ID，查询报销单对象
			BaoxiaoBill bill = baoxiaoService.findBaoxiaoBillById(id);
			model.addAttribute("baoxiaoBill", bill);
			// 2：使用报销单ID，查询历史的批注信息
			commentList = workFlowService.findCommentByBaoxiaoBillId(id);
		}
		model.addAttribute("flag", flag);
		model.addAttribute("commentList", commentList);

		return uri;
	}

	/**
	 * 删除部署信息
	 */
	@RequestMapping("/deploymentdel")
	public String delDeployment(String deploymentId) {
		// 使用部署对象ID，删除流程定义
		workFlowService.deleteProcessDefinitionByDeploymentId(deploymentId);
		return "redirect:/processDefinitionList";
	}

}
