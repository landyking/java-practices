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
public class OfficeApprovalTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        if (delegateTask.getEventName().equals("complete")) {
            System.out.println("OfficeApprovalTaskListener execute ********************");
            delegateTask.setVariable("officeViewAssigneeList", Arrays.asList("viewerOne", "viewerTwo"));
            delegateTask.setVariable("beCopyToUserList", Arrays.asList("beCopyToOne", "beCopyToTwo"));
        }
    }
}
