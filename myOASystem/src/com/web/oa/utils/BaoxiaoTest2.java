package com.web.oa.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class BaoxiaoTest2 {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	@Test
	public void deployProcessDefinition() {
		Deployment deployment = processEngine.getRepositoryService()
										.createDeployment()
										.name("报销流程测试4")
										.addClasspathResource("workflow/baoxiaoprocess.bpmn")
										.addClasspathResource("workflow/baoxiaoprocess.png")
										.deploy();
		System.out.println("部署ID： " + deployment.getId());
		System.out.println("部署名称 ： " + deployment.getName());
	}

	@Test //起动流程实例
	public void startProcess() {
		String processKey = "baoxiao2";
		ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(processKey);
		
		System.out.println("流程实例id: "+pi.getId());
		System.out.println("流程定义id: "+pi.getProcessDefinitionId());
	}
	
	@Test //查询个人任务
	public void findMyTask() {
		String assignee = "staff";
		List<Task> list = processEngine.getTaskService()
						  .createTaskQuery()
						  .taskAssignee(assignee)
						  .list();
		if (list != null && list.size()>0) {
			for (Task task : list) {
				System.out.println("任务id:" + task.getId());
				System.out.println("任务名称: " + task.getName());
				System.out.println("办理时间：" + task.getCreateTime());
				System.out.println("办理人：" + task.getAssignee());
				System.out.println("流程实例：" + task.getProcessDefinitionId());
				System.out.println("执行对象：" + task.getExecutionId());
			}
		}
	}
	
	@Test //完成个人任务
	public void finishMyTask() {
		String taskId = "7002";
		processEngine.getTaskService().complete(taskId);
		System.out.println("完成任务");
		
	}
	
	@Test //完成个人任务
	public void finishMyTask2() {
		String taskId = "7002";
		Map<String, Object> map = new HashMap<>();
		//map.put("money", 6000);
		//map.put("message", "money gt 5000");
		//map.put("message", "agree");
		//map.put("message", "refuse");
		map.put("message", "disagree");
		//processEngine.getTaskService().complete(taskId);
		processEngine.getTaskService().complete(taskId, map);
		System.out.println("完成任务");
		
	}
}
