package com.github.landyking.learnActiviti.flower;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/24 10:02
 * note:
 */
public class FlowerExecutionListener implements ExecutionListener, ApplicationContextAware {
    private ApplicationContext app;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String eventName = execution.getEventName();
        Map<String, Object> variables = execution.getVariables();
        String processBusinessKey = execution.getProcessBusinessKey();
        Map<String, Object> variablesLocal = execution.getVariablesLocal();
        System.out.println("############## eventName: " + eventName + ", processInstanceId: " + execution.getProcessInstanceId() +
                ", businessKey: " + processBusinessKey + ", variables: " + variables.toString() + ", variablesLocal: " + variablesLocal.toString());
        if ("end".equals(eventName)) {
            Map<String, FlowerCallback> beans = app.getBeansOfType(FlowerCallback.class);
            for (FlowerCallback one : beans.values()) {
                one.onExecutionEnd(execution);
            }
        } else if ("start".equals(eventName)) {
            Map<String, FlowerCallback> beans = app.getBeansOfType(FlowerCallback.class);
            for (FlowerCallback one : beans.values()) {
                one.onExecutionStart(execution);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.app = applicationContext;
    }
}
