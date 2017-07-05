package com.github.landyking.learnActiviti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;


/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/7/5 16:52
 * note:
 */
public class TaskListenerTest {
    @BeforeClass
    public static void beforeAll() {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
    }

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void test() throws Exception {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("taskListenerTest.bpmn20.xml").deploy();
        System.out.println("部署成功：" + deploy.getId() + "," + deploy.getName());
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("taskListenerTest", "myItemId");
        System.out.println("启动流程实例:" + instance.getId() + "," + instance.getName() + "," + instance.getBusinessKey());
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(instance.getProcessInstanceId()).singleResult();
        Assert.assertNotNull(task);
        Assert.assertEquals("受托人不一致",MyTaskListener.USER_NAME, task.getAssignee() );
        taskService.complete(task.getId());
    }
}
