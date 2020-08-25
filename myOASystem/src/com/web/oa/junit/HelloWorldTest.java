package com.web.oa.junit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * `act_re_procdef` 流程定义表 process definition,相当于“模板” `act_re_deployment` 流程部署信息表
 * `act_ge_bytearray` 流程的资源表（存放二进制数据） `act_hi_procinst` 流程实例历史表 process
 * instance，相当于“某一个流程的具体操作” history `act_ru_execution` 流程实例的执行对象表 `act_ru_task`
 * 当前活动的任务（节点），默认情况，`act_ru_task和`act_ru_execution`有一对一的关系 `act_hi_taskinst`
 * 历史任务记录表
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml", "classpath:spring/springmvc.xml" })
public class HelloWorldTest {

	// 连接数据库，默认方式是使用spring方式，使用activiti.cfg.xml配置文件
	// ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	// @Autowired
	// private FormService formService;
	// @Autowired
	// private HistoryService historyService;

	// 部署流程图
	@Test
	public void testDeployProcess() {
		Deployment deployment = repositoryService.createDeployment().name("helloworld入门程序")
				.addClasspathResource("diagram/helloworld.bpmn").addClasspathResource("diagram/helloworld.png")
				.deploy();

		System.out.println("部署对象 ID: " + deployment.getId());
		System.out.println("部署对象 Name : " + deployment.getName());
	}

	// 部署流程图
	@Test
	public void testDeployProcess2() throws FileNotFoundException {
		InputStream inputstream = new FileInputStream("D:\\workflow\\helloworld.zip");
		ZipInputStream zipInputstream = new ZipInputStream(inputstream);
		Deployment deployment = this.repositoryService.createDeployment().name("helloworld入门程序")
				.addZipInputStream(zipInputstream).deploy();

		System.out.println("部署对象 ID: " + deployment.getId());
		System.out.println("部署对象 Name : " + deployment.getName());
	}

	// 启动流程实例
	@Test
	public void testStartProcess() {
		String key = "helloworldProcess";
		ProcessInstance pi = this.runtimeService.startProcessInstanceByKey(key);

		System.out.println("流程实例的ID: " + pi.getId());
		System.out.println("流程定义的ID: " + pi.getProcessDefinitionId());// helloworldProcess:1:4
	}

	// 查看任务人的待办事务
	@Test
	public void testTaskList() {
		String assignee = "boss";
		List<Task> list = this.taskService.createTaskQuery().taskAssignee(assignee).list();
		for (Task task : list) {
			System.out.println("Task Id: " + task.getId());
			System.out.println("Task Assignee: " + task.getAssignee());
			System.out.println("Task Time : " + task.getCreateTime());
			System.out.println("Process Definition id: " + task.getProcessDefinitionId());
			System.out.println("Process Instance id: " + task.getProcessInstanceId());
		}
	}

	// 完成待办事务
	@Test
	public void testFinishTask() {
		String taskId = "202";
		this.taskService.complete(taskId);
		System.out.println("任务完成");
	}

	// 查看应用的流程定义
	@Test
	public void testFindProcessDef() {
		List<ProcessDefinition> list = this.repositoryService.createProcessDefinitionQuery()
				.orderByProcessDefinitionName().desc().list();
		for (ProcessDefinition pd : list) {
			System.out.println("流程定义的ID ： " + pd.getId());
			System.out.println("流程定义的key ： " + pd.getKey());
			System.out.println("流程定义的版本 ： " + pd.getVersion());
			System.out.println("流程定义的部署ID ： " + pd.getDeploymentId());
		}
	}

}
