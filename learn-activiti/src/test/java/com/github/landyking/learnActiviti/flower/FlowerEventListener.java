package com.github.landyking.learnActiviti.flower;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/24 17:30
 * note:
 */
public class FlowerEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType type = event.getType();
        String processInstanceId = event.getProcessInstanceId();
        System.out.println("eventType: " + type + ", processInstanceId: " + processInstanceId);
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }
}
