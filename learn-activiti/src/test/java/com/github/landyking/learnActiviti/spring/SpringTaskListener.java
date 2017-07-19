package com.github.landyking.learnActiviti.spring;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;

import javax.annotation.Resource;

/**
 * @author: landy
 * @date: 2017-07-19 23:19
 */
public class SpringTaskListener implements TaskListener {
    @Resource
    private TaskService taskService;
    private int mark = 11;

    public int getMark() {
        return mark;
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        System.out.println("SpringTaskListener execute ******************** eventName: " + eventName);
        String assignee = delegateTask.getAssignee();
        System.out.println("Current task assignee is: " + assignee);
        System.out.println(taskService);
        mark = 12;
    }
}