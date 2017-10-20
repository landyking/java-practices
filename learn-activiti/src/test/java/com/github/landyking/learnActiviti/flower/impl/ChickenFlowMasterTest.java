package com.github.landyking.learnActiviti.flower.impl;

import com.github.landyking.learnActiviti.BasicUseEngine;
import com.github.landyking.learnActiviti.flower.Task;
import org.activiti.engine.test.ActivitiRule;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 16:25
 * note:
 */
public class ChickenFlowMasterTest {
    @BeforeClass
    public static void beforeAll() {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-1.properties"));
    }

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void test() throws Exception {
        ChickenFlowMaster master = new ChickenFlowMaster();
        master.setEngine(activitiRule.getProcessEngine());
        master.init();
        System.out.println("#################");
        master.init();
        String flowId = master.startFlow("hello", null);
        int world = master.getTaskCount("world");
        if (world > 0) {
            Task task = master.getTaskList("world").get(0);
            master.processTask("world", task.getId(), null);
        }
    }
}