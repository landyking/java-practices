package com.github.landyking.learnActiviti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
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

/**
 * @author: landy
 * @date: 2017-07-17 22:36
 */
public class CandidateTest {
    @BeforeClass
    public static void beforeAll() {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
    }

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void test1() throws Exception {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("candidateTest.bpmn20.xml").deploy();
        System.out.println("部署成功：" + deploy.getId() + "," + deploy.getName());
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        HashMap<String, Object> variables = new HashMap<String, Object>();
        variables.put("officeCandidateUsers", Arrays.asList("hello", "world", "Lucy"));
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("candidateTest", variables);

        Task officeApprovalTask = activitiRule.getTaskService().createTaskQuery()
                .processInstanceId(instance.getProcessInstanceId())
                .taskDefinitionKey("officeApproval").singleResult();
        System.out.println("officeApproval : " + officeApprovalTask.getName());
        System.out.println("candidate users : " + getTaskCandidate(officeApprovalTask.getId()));
    }

    @Test
    public void test2() throws Exception {
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
        System.out.println("officeApproval : " + officeApprovalTask.getName());
        System.out.println("candidate users : " + getTaskCandidate(officeApprovalTask.getId()));
        taskService.claim(officeApprovalTask.getId(), "hello");
        taskService.complete(officeApprovalTask.getId());

    }

    private Set<String> getTaskCandidate(String taskId) {
        Set<String> users = new HashSet<String>();
        List<IdentityLink> identityLinkList = activitiRule.getTaskService().getIdentityLinksForTask(taskId);
        if (identityLinkList != null && identityLinkList.size() > 0) {
            for (Iterator iterator = identityLinkList.iterator(); iterator
                    .hasNext(); ) {
                IdentityLink identityLink = (IdentityLink) iterator.next();
                if (identityLink.getType().equals("candidate")) {
                    if (identityLink.getUserId() != null) {
                        users.add(identityLink.getUserId());
                    }
                    if (identityLink.getGroupId() != null) {
                        // 根据组获得对应人员
                        List<User> userList = activitiRule.getIdentityService().createUserQuery()
                                .memberOfGroup(identityLink.getGroupId()).list();
                        if (userList != null && userList.size() > 0) {
                            for (User user : userList) {
                                users.add(user.getId());
                            }
                        }
                    }
                }
            }

        }
        return users;
    }
}
