package com.github.landyking.learnActiviti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Arrays;

/**
 *
 * @author: Landy
 * @date: 2017/7/7 12:00
 * note:
 */
public class CopyToEndTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getEventName().equals("complete")) {
            System.out.println("CopyToEndTaskListener execute ********************");
            String assignee = delegateTask.getAssignee();
            System.out.println("Current task assignee is: " + assignee);
            delegateTask.setVariable("copyToFeedbackUserList", Arrays.asList(assignee + "_ONE", assignee + "_TWO"));
        }
    }
}
