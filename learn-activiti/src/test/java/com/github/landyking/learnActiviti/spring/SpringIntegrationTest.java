package com.github.landyking.learnActiviti.spring;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author: landy
 * @date: 2017-07-19 23:01
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring_integration.xml")
public class SpringIntegrationTest {
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private TaskService taskService;
    @Resource
    private Printer printer;
    @Resource
    private SpringTaskListener springTaskListener;

    @Test
    public void test1() throws Exception {

        Deployment deploy = repositoryService.createDeployment().addClasspathResource("spring/spring1.bpmn20.xml").deploy();
        Assert.assertEquals(0, printer.getMark());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("helloProcess");
        System.out.println("process isEnded: " + process.isEnded());
        Assert.assertEquals(2, printer.getMark());
    }

    @Test
    public void test2() throws Exception {

        Deployment deploy = repositoryService.createDeployment().addClasspathResource("spring/spring2.bpmn20.xml").deploy();
        Assert.assertEquals(11, springTaskListener.getMark());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("helloProcess");
        System.out.println("process isEnded: " + process.isEnded());
        if (!process.isEnded()) {
            Task task = taskService.createTaskQuery().taskDefinitionKey("print").singleResult();
            if (task != null) {
                taskService.complete(task.getId());
            } else {
                System.out.println("No Task ##############");
            }
        }
        process = runtimeService.createProcessInstanceQuery().processInstanceId(process.getProcessInstanceId()).singleResult();
        boolean isEnded = process == null || process.isEnded();
        System.out.println("process isEnded: " +isEnded);
//        Assert.assertEquals(12, springTaskListener.getMark());
    }
}
