package com.github.landyking.learnActiviti.flower;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: landy
 * @date: 2017-10-23 21:53
 */
public class FlowerTaskListener implements TaskListener,ApplicationContextAware {
    private ApplicationContext app;

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        Map<String, Object> variables = delegateTask.getVariables();
        StringBuilder sb=new StringBuilder("############## eventName: " + eventName +", processInstanceId: "+delegateTask.getProcessInstanceId()+ ", taskName: " + delegateTask.getName()+", ");
        if (delegateTask.getAssignee() != null) {
            sb.append("assignee: " + delegateTask.getAssignee());
        } else {
            sb.append("candidates: " + getCandidates(delegateTask));
        }
        sb.append(", variables: " + variables.toString()+", variablesLocal: "+delegateTask.getVariablesLocal());
        System.out.println(sb.toString());
        if ("create".equals(eventName)) {
            Map<String, FlowerCallback> beans = app.getBeansOfType(FlowerCallback.class);
            for (FlowerCallback one : beans.values()) {
                one.onTaskCreate(delegateTask);
            }
        }else if ("assignment".equals(eventName)) {
            Map<String, FlowerCallback> beans = app.getBeansOfType(FlowerCallback.class);
            for (FlowerCallback one : beans.values()) {
                one.onTaskAssignment(delegateTask);
            }
        }else if ("complete".equals(eventName)) {
            Map<String, FlowerCallback> beans = app.getBeansOfType(FlowerCallback.class);
            for (FlowerCallback one : beans.values()) {
                one.onTaskComplete(delegateTask);
            }
        }
    }

    private List<String> getCandidates(DelegateTask delegateTask) {
        List<String> candidates = new ArrayList<String>();
        for (IdentityLink one : delegateTask.getCandidates()) {
            if (one.getType().equals(IdentityLinkType.CANDIDATE)) {
                if (StringUtils.hasText(one.getUserId())) {
                    candidates.add(one.getUserId());
                }
            }
        }
        return candidates;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.app=applicationContext;
    }
}
