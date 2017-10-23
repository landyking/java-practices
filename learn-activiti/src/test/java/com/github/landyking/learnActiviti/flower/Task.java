package com.github.landyking.learnActiviti.flower;

import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Set;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 11:54
 * note:
 */
public class Task {
    private String id;
    private String user;
    private Set<String> candidate;
    private String name;
    private Date startTime;
    private String processInstanceId;
    private String flowId;

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public boolean isCandidate() {
        return candidate != null;
    }

    public String getUser() {
        return user;
    }

    public Set<String> getCandidate() {
        return candidate;
    }

    public void setCandidate(Set<String> candidate) {
        this.candidate = candidate;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
