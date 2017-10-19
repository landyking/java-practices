package com.github.landyking.learnActiviti;

import com.github.landyking.learnActiviti.util.MyClaimTaskCmd;
import org.activiti.engine.ActivitiOptimisticLockingException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author: landy
 * @date: 2017-07-17 22:36
 */
public class CandidateVersionLockTest {


    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test(expected = ActivitiOptimisticLockingException.class)
    public void test2() throws Throwable {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-1.properties"));
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("candidateTest2.bpmn20.xml").deploy();
        System.out.println("部署成功：" + deploy.getId() + "," + deploy.getName());
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("officeCandidateUsers", Arrays.asList("hello", "world", "Lucy"));
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("candidateTest", variables);

        TaskService taskService = activitiRule.getTaskService();
        Task officeApprovalTask = taskService.createTaskQuery().taskCandidateUser("hello")
                .processInstanceId(instance.getProcessInstanceId())
                .taskDefinitionKey("officeApproval").singleResult();
        Task officeApprovalTask2 = taskService.createTaskQuery().taskCandidateUser("world")
                .processInstanceId(instance.getProcessInstanceId())
                .taskDefinitionKey("officeApproval").singleResult();
        System.out.println("officeApproval : " + officeApprovalTask.getName());
        ExecutorService exec = Executors.newCachedThreadPool();
        final String taskId1 = officeApprovalTask.getId();
        final String taskId2 = officeApprovalTask2.getId();
        Future<?> hello = exec.submit(new Runnable() {
            @Override
            public void run() {

                ((TaskServiceImpl) activitiRule.getTaskService()).getCommandExecutor().execute(new MyClaimTaskCmd(taskId1, "hello"));
                activitiRule.getTaskService().complete(taskId1);
            }
        });
        Future<?> world = exec.submit(new Runnable() {
            @Override
            public void run() {
                ((TaskServiceImpl) activitiRule.getTaskService()).getCommandExecutor().execute(new MyClaimTaskCmd(taskId2, "world"));
                activitiRule.getTaskService().complete(taskId2);
            }
        });
        try {
            hello.get();
            world.get();
        }catch (ExecutionException e){
            e.printStackTrace();
            throw e.getCause();
        }finally {
            exec.shutdown();
        }
    }


}
