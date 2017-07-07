package com.github.landyking.learnActiviti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/7/5 15:23
 * note:
 */
public class SubprocessTest {
    @BeforeClass
    public static void beforeAll() {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
    }

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testSubprocess() throws Exception {
        doWork("subprocessTest.bpmn20.xml");
    }

    private void doWork(String resource) {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource(resource).deploy();
        System.out.println("部署成功：" + deploy.getId() + "," + deploy.getName());
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("countersignTest", "myItemId");
        System.out.println("启动流程实例:" + instance.getId() + "," + instance.getName() + "," + instance.getBusinessKey());
        TaskService taskService = activitiRule.getTaskService();
        showCurrentTaskList(instance, taskService);
        while (instance != null && !instance.isEnded()) {
            List<Task> list = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).list();
            Task task = list.get(0);
            taskService.complete(task.getId());
            System.out.println("\n##################完成任务：" + task.getName() + "," + task.getAssignee());
            showCurrentTaskList(instance, taskService);

            instance = runtimeService.createProcessInstanceQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
        }
    }

    private void showCurrentTaskList(ProcessInstance instance, TaskService taskService) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).orderByTaskCreateTime().asc().list();
        System.out.println("当前任务列表长度:" + list.size());
        for (Task one : list) {
            System.out.println("\t_________________");
            System.out.println("\ttask id: " + one.getId());
            System.out.println("\ttask name: " + one.getName());
            System.out.println("\ttask assignee: " + one.getAssignee());
            System.out.println("\ttask local variables: " + one.getTaskLocalVariables());
            System.out.println("\ttask process variables: " + one.getProcessVariables());
            System.out.println("\ttask create time: " + Tools.longFmt(one.getCreateTime()));
        }
    }
}
