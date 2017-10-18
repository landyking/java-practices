package com.github.landyking.learnActiviti;

import com.github.landyking.learnActiviti.util.ModelRollBack;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.history.HistoryLevel;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.PropertyConfigurator;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author: landy
 * @date: 2017/7/5 9:57
 * note:
 */
public class LeaveBillRollBackTest {
    public static void main(String[] args) {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
        ProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
                .setJdbcDriver("org.h2.Driver")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
//                .setHistoryLevel(HistoryLevel.FULL)
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("leaveBill.bpmn20.xml").deploy();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        processEngine.getIdentityService().setAuthenticatedUserId("landyking");
        ProcessInstance instance = runtimeService.startProcessInstanceById(processDefinition.getId());
        String processInstanceId = instance.getId();
        System.out.println("############################################启动流程，id: " + processInstanceId +
                ", name: " + instance.getName());
        TaskService taskService = processEngine.getTaskService();
        HistoryService historyService = processEngine.getHistoryService();
        showInstance(processInstanceId, historyService);
        showTaskList(processInstanceId, taskService);
        showHistoryTaskList(processInstanceId, historyService);
        showActivityList(processInstanceId, historyService);

        System.out.println("\n\n############################################提交请假单");
        Task user = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("user").singleResult();
        taskService.complete(user.getId(), Collections.<String, Object>singletonMap("hello", "world"));
        showInstance(processInstanceId, historyService);
        showTaskList(processInstanceId, taskService);
        showHistoryTaskList(processInstanceId, historyService);
        showActivityList(processInstanceId, historyService);

        System.out.println("\n\n############################################撤销请假单");
        Task leader = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("leader").singleResult();
        new ModelRollBack(processEngine).rollBackWorkFlow(leader.getId());
        showInstance(processInstanceId, historyService);
        showTaskList(processInstanceId, taskService);
        showHistoryTaskList(processInstanceId, historyService);
        showActivityList(processInstanceId, historyService);

        System.out.println("\n\n############################################提交请假单");
        user = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("user").singleResult();
        taskService.complete(user.getId(), Collections.<String, Object>singletonMap("hello", "world"));
        showInstance(processInstanceId, historyService);
        showTaskList(processInstanceId, taskService);
        showHistoryTaskList(processInstanceId, historyService);
        showActivityList(processInstanceId, historyService);

        System.out.println("\n\n############################################上级领导审批");
        leader = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("leader").singleResult();
        taskService.complete(leader.getId(), Collections.<String, Object>singletonMap("test", "okbeng"));
        showInstance(processInstanceId, historyService);
        showTaskList(processInstanceId, taskService);
        showHistoryTaskList(processInstanceId, historyService);
        showActivityList(processInstanceId, historyService);

        System.out.println("\n\n############################################经理审批");
        Task boss = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("boss").singleResult();
        taskService.complete(boss.getId(), Collections.<String, Object>singletonMap("mama", "didadada"));
        showInstance(processInstanceId, historyService);
        showTaskList(processInstanceId, taskService);
        showHistoryTaskList(processInstanceId, historyService);
        showActivityList(processInstanceId, historyService);

    }

    private static void showTaskList(String processInstanceId, TaskService taskService) {
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        System.out.println("当前任务列表展示");
        System.out.println("\tall active task size: " + list.size());
        for (Task one : list) {
            System.out.println("\t__________________");
            System.out.println("\ttask create time: " + Tools.longFmt(one.getCreateTime()));
            System.out.println("\ttask due date: " + one.getDueDate());
            System.out.println("\ttask name: " + one.getName());
            System.out.println("\ttask assignee: " + one.getAssignee());
            System.out.println("\ttask definition key: " + one.getTaskDefinitionKey());
            System.out.println("\ttask execution id: " + one.getExecutionId());
            System.out.println("\ttask id: " + one.getId());
        }
    }

    private static void showActivityList(String processInstanceId, HistoryService historyService) {
        System.out.println("历史活动列表展示");
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        System.out.println("\tall activity size: " + list.size());
        for (HistoricActivityInstance one : list) {
            System.out.println("\t_______________");
            System.out.println("\tactivity start time: " + Tools.longFmt(one.getStartTime()));
            System.out.println("\tactivity task id: " + one.getTaskId());
            System.out.println("\tactivity id: " + one.getActivityId());
            System.out.println("\tactivity name: " + one.getActivityName());
            System.out.println("\tactivity assignee: " + one.getAssignee());
            System.out.println("\tactivity type: " + one.getActivityType());
            System.out.println("\tactivity end time: " + Tools.longFmt(one.getEndTime()));

        }
    }

    private static void showHistoryTaskList(String processInstanceId, HistoryService historyService) {
        System.out.println("历史任务列表展示");
        List<HistoricTaskInstance> historyTaskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .orderByTaskCreateTime().asc().list();
        System.out.println("\tall historic task size: " + historyTaskList.size());
        for (HistoricTaskInstance historyTask : historyTaskList) {
            System.out.println("\t___________");
            System.out.println("\thistory task id: " + historyTask.getId());
            System.out.println("\thistory task start time: " + Tools.longFmt(historyTask.getStartTime()));
            System.out.println("\thistory task name: " + historyTask.getName());
            System.out.println("\thistory task description: " + historyTask.getDescription());
            System.out.println("\thistory task end time: " + Tools.longFmt(historyTask.getEndTime()));
        }
    }

    private static void showInstance(String processInstanceId, HistoryService historyService) {
        System.out.println("流程实例详情");
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        System.out.println("\tinstance name: " + historicProcessInstance.getName());
        System.out.println("\tinstance start time: " + Tools.longFmt(historicProcessInstance.getStartTime()));
        System.out.println("\tinstance start user id: " + historicProcessInstance.getStartUserId());
        System.out.println("\tinstance variables: " + historicProcessInstance.getProcessVariables().toString());
        System.out.println("\tinstance end time: " + Tools.longFmt(historicProcessInstance.getEndTime()));
    }
}
