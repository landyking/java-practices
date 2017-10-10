package com.github.landyking.learnActiviti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
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
public class ActivityTest {
    @BeforeClass
    public static void beforeAll() {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
    }

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void test1() throws Exception {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("onboarding.bpmn20.xml").deploy();
        System.out.println("部署成功：" + deploy.getId() + "," + deploy.getName());
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("onboarding").singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(definition.getId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();

        for (ActivityImpl activity : activitiList) {
            Map<String, Object> properties = activity.getProperties();
            String type = properties.get("type").toString();
            if (type.equals("exclusiveGateway")) {
                List<PvmTransition> outgoingTransitions = activity.getOutgoingTransitions();
                for (PvmTransition outgoingTransition : outgoingTransitions) {
                    TransitionImpl ti = (TransitionImpl) outgoingTransition;
                    System.out.println(ti.getId()+" # "+ti.getProperties());
                }
            } else {
                System.out.println(properties);
            }
        }
    }

}
