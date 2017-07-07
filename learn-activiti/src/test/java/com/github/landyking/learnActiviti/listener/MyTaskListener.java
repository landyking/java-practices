package com.github.landyking.learnActiviti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.UUID;

/**
 *
 * @author: Landy
 * @date: 2017/7/5 16:55
 * note:
 */
public class MyTaskListener implements TaskListener {
    public static final String USER_NAME = UUID.randomUUID().toString();

    @Override
    public void notify(DelegateTask delegateTask) {
        delegateTask.setAssignee(USER_NAME);
        System.out.println("set task assignee: " + USER_NAME);
    }
}
