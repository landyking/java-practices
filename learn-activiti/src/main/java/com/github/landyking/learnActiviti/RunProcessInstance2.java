package com.github.landyking.learnActiviti;

import org.activiti.engine.*;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.PropertyConfigurator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 参考链接 https://www.activiti.org/quick-start
 */
public class RunProcessInstance2 {
    public static void main(String[] args) throws ParseException {
        //部署
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .setJdbcDriver("org.h2.Driver")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println("ProcessEngine [" + processEngine.getName() + "] Version: [" + ProcessEngine.VERSION + "]");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("onboarding2.bpmn20.xml").deploy();
        System.out.println("deploy: id: " + deploy.getId() + ", name: " + deploy.getName() + ", category: " + deploy.getCategory() + ", tenantId: " + deploy.getTenantId());

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        System.out.println("ProcessDefinition" +
                "\n\tid: " + processDefinition.getId() +
                "\n\tdeployId: " + processDefinition.getDeploymentId() +
                "\n\tcategory: " + processDefinition.getCategory() +
                "\n\ttenantId: " + processDefinition.getTenantId() +
                "\n\tdescription:" + processDefinition.getDescription());
        RuntimeService runtimeService = processEngine.getRuntimeService();
        FormService formService = processEngine.getFormService();
        HistoryService historyService = processEngine.getHistoryService();
        IdentityService identityService = processEngine.getIdentityService();
        ManagementService managementService = processEngine.getManagementService();
        TaskService taskService = processEngine.getTaskService();

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("onboarding", "myItemId");
        System.out.println("Start process by key: onboarding, instance id: " + processInstance.getId() + ", businessKey: " + processInstance.getBusinessKey());
        Scanner scanner = new Scanner(System.in);
        while (processInstance != null && !processInstance.isEnded()) {
            List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
            System.out.println("task size: " + tasks.size());
            for (Task task : tasks) {
                System.out.println("Processing task: " + task.getName());
                HashMap<String, Object> variables = new HashMap<String, Object>();
                TaskFormData taskFormData = formService.getTaskFormData(task.getId());
                for (FormProperty formProperty : taskFormData.getFormProperties()) {
                    if (StringFormType.class.isInstance(formProperty.getType())) {
                        System.out.println(formProperty.getName() + "?");
                        String txt = scanner.nextLine();
                        variables.put(formProperty.getId(), txt);
                    } else if (LongFormType.class.isInstance(formProperty.getType())) {
                        System.out.println(formProperty.getName() + "? (Must be a whole number)");
                        Long value = Long.valueOf(scanner.nextLine());
                        variables.put(formProperty.getId(), value);
                    } else if (DateFormType.class.isInstance(formProperty.getType())) {
                        System.out.println(formProperty.getName() + "? (Must be a date yyyy/MM/dd)");
                        String line = scanner.nextLine();
                        variables.put(formProperty.getId(), new SimpleDateFormat("yyyy/MM/dd").parse(line));
                    } else {
                        System.out.println("form type not supported: " + formProperty.getType().getName());
                    }
                }

                taskService.complete(task.getId(), variables);

                List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId()).finished()
                        .orderByHistoricActivityInstanceEndTime().desc()
                        .list();
                for (HistoricActivityInstance history : historicActivityInstances) {
                    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(history.getStartTime()) + " -- " + history.getActivityName() + "[id:" + history.getActivityId() + "] " + history.getDurationInMillis() + " ms");
                }
            }

            processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
        }
        scanner.close();
    }
}