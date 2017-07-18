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

import java.util.HashMap;
import java.util.List;

/**
 * @author: landy
 * @date: 2017/7/5 9:57
 * note:
 */
public class RevokeLeaveAndEndTest {
    @BeforeClass
    public static void beforeAll() {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
    }

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testRevoke1() throws Exception {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("revokeLeaveBill.bpmn20.xml").deploy();
        System.out.println("部署成功：" + deploy.getId() + "," + deploy.getName());
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("revokeLeaveBill","business key 0001");
        System.out.println("启动流程实例:" + instance.getId() + "," + instance.getName() + "," + instance.getBusinessKey());
        TaskService taskService = activitiRule.getTaskService();
        showCurrentTaskList(instance, taskService);
        Task createBill = taskService.createTaskQuery().taskDefinitionKey("createBill").singleResult();
        taskService.complete(createBill.getId());
        System.out.println("##### 完成任务：" + createBill.getName());
        showCurrentTaskList(instance, taskService);
        showProcessDone(runtimeService, instance);
        Task leaderApproval = taskService.createTaskQuery().taskDefinitionKey("leaderApproval").singleResult();
        taskService.complete(leaderApproval.getId());
        System.out.println("#### 完成任务：" + leaderApproval.getName());
        Task bossApproval = taskService.createTaskQuery().taskDefinitionKey("bossApproval").singleResult();
        HashMap<String, Object> vars = new HashMap<String, Object>();
        vars.put("revoked", false);
        taskService.complete(bossApproval.getId(),vars);
        System.out.println("#### 完成任务：" + bossApproval.getName());
        showCurrentTaskList(instance, taskService);
        showProcessDone(runtimeService, instance);
    }
    @Test
    public void testRevoke2() throws Exception {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("revokeLeaveBill.bpmn20.xml").deploy();
        System.out.println("部署成功：" + deploy.getId() + "," + deploy.getName());
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("revokeLeaveBill","business key 0001");
        System.out.println("启动流程实例:" + instance.getId() + "," + instance.getName() + "," + instance.getBusinessKey());
        TaskService taskService = activitiRule.getTaskService();
        showCurrentTaskList(instance, taskService);
        Task createBill = taskService.createTaskQuery().taskDefinitionKey("createBill").singleResult();
        taskService.complete(createBill.getId());
        System.out.println("##### 完成任务：" + createBill.getName());
        showCurrentTaskList(instance, taskService);
        showProcessDone(runtimeService, instance);
        Task bossApproval = taskService.createTaskQuery().taskDefinitionKey("revokeApply").singleResult();
        HashMap<String, Object> vars = new HashMap<String, Object>();
        vars.put("revoked", true);
        taskService.complete(bossApproval.getId(),vars);
        System.out.println("#### 完成任务：" + bossApproval.getName());
        showCurrentTaskList(instance, taskService);
        showProcessDone(runtimeService, instance);
    }

    private void showProcessDone(RuntimeService runtimeService, ProcessInstance instance) {
        instance = runtimeService.createProcessInstanceQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
        System.out.println("!!!!!!!!!!!!!!!实例完成：" + (instance == null));
    }


    private void doWork(String resource) {

    }

    private void showCurrentTaskList(ProcessInstance instance, TaskService taskService) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).orderByTaskCreateTime().asc().list();
        System.out.println("当前任务列表长度:" + list.size());
        for (Task one : list) {
            System.out.println("\t_________________");
            System.out.println("\ttask id: " + one.getId());
            System.out.println("\ttask definition key: " + one.getTaskDefinitionKey());
            System.out.println("\ttask name: " + one.getName());
            System.out.println("\ttask assignee: " + one.getAssignee());
            System.out.println("\ttask form key: " + one.getFormKey());
            System.out.println("\ttask local variables: " + one.getTaskLocalVariables());
            System.out.println("\ttask process variables: " + one.getProcessVariables());
            System.out.println("\ttask create time: " + Tools.longFmt(one.getCreateTime()));
        }
    }
}
