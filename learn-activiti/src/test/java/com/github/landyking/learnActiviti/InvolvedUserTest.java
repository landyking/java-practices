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
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/7/20 17:22
 * note:
 */
public class InvolvedUserTest {
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
        Task createBill = taskService.createTaskQuery().taskDefinitionKey("createBill").singleResult();
        taskService.complete(createBill.getId());
        System.out.println("################ complete task createBill");
        long involvedCount = taskService.createTaskQuery().taskInvolvedUser("leader").count();
        System.out.println("involvedCount involved task count : "+involvedCount);
        long assigneeCount = taskService.createTaskQuery().taskAssignee("leader").count();
        System.out.println("involvedCount assignee task count : "+assigneeCount);
        long candidateCount = taskService.createTaskQuery().taskCandidateUser("leader").count();
        System.out.println("involvedCount candidate task count : "+candidateCount);
        long candidateOrAssignedCount = taskService.createTaskQuery().taskCandidateOrAssigned("leader").count();
        System.out.println("involvedCount candidateOrAssigned task count : "+candidateOrAssignedCount);
    }

}
