package com.github.landyking.learnActiviti.flower;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: landy
 * @date: 2017-10-23 21:53
 */
public class FlowerTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        Map<String, Object> variables = delegateTask.getVariables();
        if ("create".equals(eventName)) {
//            Assert.isTrue(1==2);
            System.out.println("##################create " + delegateTask.getName());
            if (delegateTask.getAssignee() != null) {
                System.out.println("assignee: " + delegateTask.getAssignee());
            } else {
                System.out.println("candidates: " + getCandidates(delegateTask));
            }
        } else if ("complete".equals(eventName)) {
            System.out.println("##################complete " + delegateTask.getName());
            if (delegateTask.getAssignee() != null) {
                System.out.println("assignee: " + delegateTask.getAssignee());
            } else {
                System.out.println("candidates: " + getCandidates(delegateTask));
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
}
