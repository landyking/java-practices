package com.github.landyking.learnActiviti.flower;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/24 10:33
 * note:
 */
public interface FlowerCallback {
    /**
     * @param execution DelegateExecution
     */
    void onExecutionEnd(Object execution);

    /**
     * @param execution DelegateExecution
     */
    void onExecutionStart(Object execution);

    /**
     * @param delegateTask DelegateTask
     */
    void onTaskCreate(Object delegateTask);

    /**
     * @param delegateTask DelegateTask
     */
    void onTaskAssignment(Object delegateTask);

    /**
     * @param delegateTask DelegateTask
     */
    void onTaskComplete(Object delegateTask);
}
