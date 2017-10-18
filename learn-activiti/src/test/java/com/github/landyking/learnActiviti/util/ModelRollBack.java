package com.github.landyking.learnActiviti.util;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelRollBack {
    private Logger log = LogManager.getLogger(ModelRollBack.class);//日志文件
    private RuntimeService runtimeService;//
    private TaskService taskService;
    private RepositoryService repositoryService;
    private HistoryService historyService;

    private final ProcessEngine processEngine;

    public ModelRollBack(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public ModelRollBack() {
        processEngine = ProcessEngines.getDefaultProcessEngine();
    }

    //退回上一个节点
    public String rollBackWorkFlow(String taskId) {

        this.taskService = processEngine.getTaskService();
        this.historyService = processEngine.getHistoryService();
        this.repositoryService = processEngine.getRepositoryService();
        this.runtimeService = processEngine.getRuntimeService();
        try {
            Map<String, Object> variables;
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(taskId)
                    .singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId())
                    .singleResult();
            if (instance == null) {
                log.info("流程结束");
                log.error("出错啦！流程已经结束");
                return "ERROR";
            }
            variables = instance.getProcessVariables();
            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(currTask
                            .getProcessDefinitionId());
            if (definition == null) {
                log.info("流程定义未找到");
                log.error("出错啦！流程定义未找到");
                return "ERROR";
            }
            // 取得上一步活动
            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                    .findActivity(currTask.getTaskDefinitionKey());

            //也就是节点间的连线
            List<PvmTransition> nextTransitionList = currActivity
                    .getIncomingTransitions();
            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            //新建一个节点连线关系集合

            List<PvmTransition> pvmTransitionList = currActivity
                    .getOutgoingTransitions();
            //
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
                        .findActivity(nextActivity.getId());
                TransitionImpl newTransition = currActivity
                        .createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }
            // 完成任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
//                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {
                pvmTransitionList.add(pvmTransition);
            }
            log.info("OK");
            log.info("流程结束");

            return "SUCCESS";
        } catch (Exception e) {
            log.error("失败");
            log.error(e.getMessage());
            return "ERROR";
        }
    }


    //回退到指定节点
    public String rollBackToAssignWorkFlow(String taskId, String destTaskkey) {
        this.taskService = processEngine.getTaskService();
        this.historyService = processEngine.getHistoryService();
        this.repositoryService = processEngine.getRepositoryService();
        this.runtimeService = processEngine.getRuntimeService();
        try {
            Map<String, Object> variables;
            // 取得当前任务.当前任务节点
            HistoricTaskInstance currTask = historyService
                    .createHistoricTaskInstanceQuery().taskId(taskId)
                    .singleResult();
            // 取得流程实例，流程实例
            ProcessInstance instance = runtimeService
                    .createProcessInstanceQuery()
                    .processInstanceId(currTask.getProcessInstanceId())
                    .singleResult();
            if (instance == null) {
                log.info("流程结束");
                log.error("出错啦！流程已经结束");
                return "ERROR";
            }
            variables = instance.getProcessVariables();
            // 取得流程定义
            ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(currTask
                            .getProcessDefinitionId());
            if (definition == null) {
                log.info("流程定义未找到");
                log.error("出错啦！流程定义未找到");
                return "ERROR";
            }
            //取得当前活动节点
            ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
                    .findActivity(currTask.getTaskDefinitionKey());

            log.info("currActivity" + currActivity);
            // 取得上一步活动
            //也就是节点间的连线
            //获取来源节点的关系
            List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();

            // 清除当前活动的出口
            List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
            //新建一个节点连线关系集合
            //获取出口节点的关系
            List<PvmTransition> pvmTransitionList = currActivity
                    .getOutgoingTransitions();
            //
            for (PvmTransition pvmTransition : pvmTransitionList) {
                oriPvmTransitionList.add(pvmTransition);
            }
            pvmTransitionList.clear();

            // 建立新出口
            List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
            for (PvmTransition nextTransition : nextTransitionList) {
                PvmActivity nextActivity = nextTransition.getSource();

                log.info("nextActivity" + nextActivity);

                log.info("nextActivity.getId()" + nextActivity.getId());

                //destTaskkey
                ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition)
                        // .findActivity(nextActivity.getId());
                        .findActivity(destTaskkey);
                TransitionImpl newTransition = currActivity
                        .createOutgoingTransition();
                newTransition.setDestination(nextActivityImpl);
                newTransitions.add(newTransition);
            }
            // 完成任务
            List<Task> tasks = taskService.createTaskQuery()
                    .processInstanceId(instance.getId())
                    .taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
            for (Task task : tasks) {
                taskService.complete(task.getId(), variables);
                historyService.deleteHistoricTaskInstance(task.getId());
            }
            // 恢复方向
            for (TransitionImpl transitionImpl : newTransitions) {
                currActivity.getOutgoingTransitions().remove(transitionImpl);
            }
            for (PvmTransition pvmTransition : oriPvmTransitionList) {

                pvmTransitionList.add(pvmTransition);
            }
            log.info("OK");
            log.info("流程结束");

            return "SUCCESS";
        } catch (Exception e) {
            log.error("失败");
            log.error(e.getMessage());
            return "ERROR";
        }
    }


    public static void main(String[] args) {

        ModelRollBack back = new ModelRollBack();
        //back.rollBackWorkFlow("1102");

        back.rollBackToAssignWorkFlow("2402", "usertask1");
    }

}