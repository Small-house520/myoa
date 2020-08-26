package com.web.oa.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.oa.pojo.ActiveUser;
import com.web.oa.pojo.Leavebill;
import com.web.oa.service.LeaveBillService;
import com.web.oa.service.WorkFlowService;

@Controller
public class LeaveBillController {

	@Autowired
	private LeaveBillService leaveBillService;

	@Autowired
	private WorkFlowService workFlowService;

	// 根据id删除请假单信息
	@RequestMapping("/leavebilldel")
	public String leavebilldel(Long id) {
		this.leaveBillService.deleteLeavebill(id);
		return "redirect:/myLeaveBill";
	}

	// 查询出请假单
	@RequestMapping("/myLeaveBill")
	public String findBaoxiaoBill(HttpSession session, Model model) {
		// 获取ActiveUser中的员工id
		long id = ((ActiveUser) SecurityUtils.getSubject().getPrincipal()).getId();
		// 根据员工id查询出请假单信息
		List<Leavebill> leavebills = this.workFlowService.findBaoxiaoBill(id);
		// 把查询出的数据设置到model
		model.addAttribute("leavebills", leavebills);

		return "jsp/leave_bill";
	}

}
