package com.github.landyking.learnActiviti.flower.impl;

import java.util.Date;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 16:13
 * note:
 */
public class TestData {
    private int status;
    private String id;
    private int count;
    private String name;
    private String processInstanceId;
    private int endFlag;
    private Date startTime;
    private Date endTime;
    private String starter;
    private int revokeFlag;

    public int getRevokeFlag() {
        return revokeFlag;
    }

    public void setRevokeFlag(int revokeFlag) {
        this.revokeFlag = revokeFlag;
    }

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public int getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(int endFlag) {
        this.endFlag = endFlag;
    }

    @Override
    public String toString() {
        return "TestData{" +
                "status=" + status +
                ", id='" + id + '\'' +
                ", count=" + count +
                ", name='" + name + '\'' +
                ", processInstanceId='" + processInstanceId + '\'' +
                ", endFlag=" + endFlag +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", starter='" + starter + '\'' +
                '}';
    }
}
