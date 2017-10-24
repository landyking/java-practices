package com.github.landyking.learnActiviti.flower.impl;

import com.github.landyking.learnActiviti.BasicUseEngine;
import com.github.landyking.learnActiviti.flower.FlowMaster;
import com.github.landyking.learnActiviti.flower.Task;
import com.github.landyking.learnActiviti.flower.Tuple;
import com.google.common.collect.Maps;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 16:25
 * note:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/flower/flower.xml")
public class TestDataFlowMasterTest {
    @BeforeClass
    public static void beforeAll() {
        PropertyConfigurator.configure(BasicUseEngine.class.getResourceAsStream("/log4j-2.properties"));
    }

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private FlowMaster<TestData> master;

    @Test
    public void test22() throws Exception {
        int count = jdbcTemplate.queryForObject("select count(*) from t_data", Number.class).intValue();
        System.out.println("data count: " + count);
    }

    @Test
    public void testInit() throws Exception {
        ((TestDataFlowMaster) master).init();
        System.out.println("#################");
        ((TestDataFlowMaster) master).init();
    }


    @Test
    public void showUserTaskList() throws Exception {
        String name = "user";
        showTaskList(name);
    }

    @Test
    public void showLeaderTaskList() throws Exception {
        String name = "leader";
        showTaskList(name);
    }

    @Test
    public void showBossTaskList() throws Exception {
        String name = "boss";
        showTaskList(name);
    }

    private void showTaskList(String name) {
        long world = master.getTaskCount(name);
        System.out.println(name + "待处理的任务数量: " + world);
        if (world > 0) {
            List<Task> taskList = master.getTaskList(name, 0, 100);
            displayTaskList(taskList);
        }
    }

    private void displayTaskList(List<Task> taskList) {
        for (Task task : taskList) {
            if (task.isCandidate()) {
                System.out.println("flowId:" + task.getFlowId() + ",实例：" + task.getProcessInstanceId() + "，任务ID:" + task.getId() + "，候选人：" + task.getCandidate().toString() + "，任务名称：" + task.getName() + "，任务开始时间：" + task.getStartTime());
            } else {
                System.out.println("flowId:" + task.getFlowId() + ",实例：" + task.getProcessInstanceId() + "，任务ID:" + task.getId() + "，所属人：" + task.getUser() + "，任务名称：" + task.getName() + "，任务开始时间：" + task.getStartTime());
            }
        }
    }

    @Test
    public void processUserTaskLIst() {
        String name = "user";
        processTaskList(name);
    }

    @Test
    public void processBossTaskLIst() {
        String name = "boss";
        processTaskList(name);
    }

    @Test
    public void processLeaderTaskLIst() {
        String name = "leader";
        processTaskList(name);
    }

    private void processTaskList(String name) {
        List<Task> taskList = master.getTaskList(name, 0, 100);
        for (Task task : taskList) {
            master.processTask(name, task.getId(), null);
            System.out.println(name + " 完成任务" + task.getId() + ":" + task.getName());
        }
    }

    @Test
    public void startNewFlow() throws Exception {
        Map<String, Object> props = Maps.newHashMap();
        props.put("count", 100);
        props.put("name", "momou");
        props.put("list", Arrays.asList("2", "3", "4"));
        String flowId = master.startFlow("hello", props);
        System.out.println("开启新流程，id为: " + flowId);
    }

    @Test
    public void showUnprocessTaskList() throws Exception {
        long world = master.getTaskCount(null);
        System.out.println("待处理的任务数量: " + world);
        if (world > 0) {
           /* List<Task> taskList = master.getTaskList(null, 0, 100);
            displayTaskList(taskList);*/
            List<Tuple<Task, TestData>> list = master.getTaskWithBusinessDataList(null, 0, 100);
            displayTaskWithBusinessDataList(list);
        }
    }

    private void displayTaskWithBusinessDataList(List<Tuple<Task, TestData>> list) {
        for (Tuple<Task, TestData> one : list) {
            Task task = one.getFirst();
            if (task.isCandidate()) {
                System.out.println("flowId:" + task.getFlowId() + ",实例：" + task.getProcessInstanceId() + "，任务ID:" + task.getId() + "，候选人：" + task.getCandidate().toString() + "，任务名称：" + task.getName() + "，任务开始时间：" + task.getStartTime());
            } else {
                System.out.println("flowId:" + task.getFlowId() + ",实例：" + task.getProcessInstanceId() + "，任务ID:" + task.getId() + "，所属人：" + task.getUser() + "，任务名称：" + task.getName() + "，任务开始时间：" + task.getStartTime());
            }
        }
    }

    @Test
    public void stopFlow() throws Exception {
        master.stopFlow("admin", "6e9bc0acd9af4ca8bbd0898a934d864e");
    }

    @Test
    public void deleteProcessInstance() throws Exception {
        ((TestDataFlowMaster) master).deleteProcessInstance("admin", "115001");
    }

    @Test
    public void showAllUnfinishProcess() throws Exception {
        long unfinishProcessCount = master.getUnfinishProcessCount();
        System.out.println("未完成的流程数量: " + unfinishProcessCount);
    }
}