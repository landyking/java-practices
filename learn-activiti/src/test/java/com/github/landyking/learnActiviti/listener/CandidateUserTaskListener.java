package com.github.landyking.learnActiviti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.IdentityLinkType;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/7/21 14:30
 * note:
 */
public class CandidateUserTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        if ("create".equals(eventName)) {
            System.out.println("##################create");
            System.out.println("assignee: " + delegateTask.getAssignee());
            System.out.println("candidates: " + getCandidates(delegateTask));
        } else if ("complete".equals(eventName)) {
            System.out.println("##################complete");
            System.out.println("assignee: " + delegateTask.getAssignee());
            System.out.println("candidates: " + getCandidates(delegateTask));
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
