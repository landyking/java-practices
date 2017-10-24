package com.github.landyking.learnActiviti.flower;

import java.util.Date;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 11:51
 * note:
 */
public class Track {
    private String id;
    private String userId;
    private String opinion;
    private int operate;
    private Date startTime;
    private Date endTime;
    private String title;
    private int type;
    private String flowId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public int getOperate() {
        return operate;
    }

    public void setOperate(int operate) {
        this.operate = operate;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", opinion='" + opinion + '\'' +
                ", operate=" + operate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", flowId='" + flowId + '\'' +
                '}';
    }
}
