package com.web.oa.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.BaoxiaoBill;
import com.web.oa.service.BaoxiaoService;
import com.web.oa.service.WorkFlowService;
import com.web.oa.utils.Constants;

@Controller
public class BaoxiaoBillController {

	@Autowired
	private BaoxiaoService baoxiaoService;

	@Autowired
	private WorkFlowService workFlowService;

	@RequestMapping("/main")
	public String main(ModelMap model) {
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		model.addAttribute("activeUser", activeUser);

		return "index";

	}

	// 根据id删除请假单信息
	@RequestMapping("/baoxiaobilldel")
	public String baoxiaobilldel(Long id) {
		this.baoxiaoService.deleteBaoxiaobill(id);
		return "redirect:/myBaoxiaoBill";
	}

	/**
	 * 显示我的报销单列表
	 * 
	 * @return
	 */
	@RequestMapping("/myBaoxiaoBill")
	public String home(ModelMap model, HttpSession session) {
		// 1：查询所有的请假信息（对应a_leavebill），返回List<LeaveBill>
		// Employee emp = (Employee)
		// session.getAttribute(Constants.GLOBLE_USER_SESSION);
		ActiveUser activeUser = (ActiveUser) SecurityUtils.getSubject().getPrincipal();
		List<BaoxiaoBill> list = baoxiaoService.findLeaveBillListByUser(activeUser.getId());
		// 放置到上下文对象中
		model.addAttribute("baoxiaoList", list);
		return "jsp/baoxiaobill";
	}

	@RequestMapping("/viewCurrentImageByBill")
	public String viewCurrentImageByBill(long billId, ModelMap model) {
		String BUSSINESS_KEY = Constants.BAOXIAO_KEY + "." + billId;
		Task task = this.workFlowService.findTaskByBussinessKey(BUSSINESS_KEY);
		/** 一：查看流程图 */
		// 1：获取任务ID，获取任务对象，使用任务对象获取流程定义ID，查询流程定义对象
		ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(task.getId());

		model.addAttribute("deploymentId", pd.getDeploymentId());
		model.addAttribute("imageName", pd.getDiagramResourceName());
		/** 二：查看当前活动，获取当期活动对应的坐标x,y,width,height，将4个值存放到Map<String,Object>中 */
		Map<String, Object> map = workFlowService.findCoordingByTask(task.getId());

		model.addAttribute("acs", map);
		return "jsp/viewimage";
	}
}
