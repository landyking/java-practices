package com.github.landyking.learnActiviti.flower;

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

    T getBusinessData(String flowId);

    long getBusinessDataCount(String starter, long position, Object... others);

    long getJoinBusinessDataCount(String user, long position, boolean includeStarter, Object... others);

    List<T> getBusinessDataList(String starter, long position, int first, int limit, Object... others);

    List<T> getJoinBusinessDataList(String user, long position, boolean includeStarter, int first, int limit, Object... others);

    List<Track> getTrackList(String flowId);

    long getTaskCount(String user, long position);

    List<Task> getTaskList(String user, long position, int first, int limit);

    List<Tuple<Task, T>> getTaskWithBusinessDataList(String user, long position, int first, int limit);

    void stopFlow(String user, String flowId);
}
