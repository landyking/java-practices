package com.github.landyking.learnActiviti.flower;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 11:40
 * note:
 */
public interface FlowMaster<T> {
    int OPERATE_PASS = 1;
    int OPERATE_REJECT = 2;
    int OPERATE_STOP = 2;

    String flowDefineId();

    String flowDefineDesc();

    String startFlow(String user, Map<String, Object> props);

    void processTask(String user, String taskId, Map<String, Object> props);

    void revokeFlow(String user, String flowId);

    T getDetail(String user, String flowId);

    List<Track> getTrackList(String user, String flowId);

    int getTaskCount(String user);

    List<Task> getTaskList(String user, int first, int limit);

    void stopFlow(String user);

}
