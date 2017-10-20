package com.github.landyking.learnActiviti.flower.impl;

import com.github.landyking.learnActiviti.flower.FlowMaster;
import com.github.landyking.learnActiviti.flower.Task;
import com.github.landyking.learnActiviti.flower.Track;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 16:19
 * note:
 */
public class ChickenFlowMaster implements FlowMaster<Chicken> {
    private Logger logger = LoggerFactory.getLogger(ChickenFlowMaster.class);
    private ProcessEngine engine;

    public ProcessEngine getEngine() {
        return engine;
    }

    public void setEngine(ProcessEngine engine) {
        this.engine = engine;
    }

    public void init() throws IOException {
        logger.debug("开始自动更新流程文件");
        ProcessDefinition leaveBill = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("leaveBill").latestVersion().singleResult();
        if (leaveBill != null) {
            InputStream processDiagram = engine.getRepositoryService().getProcessModel(leaveBill.getId());
            if (processDiagram != null) {
                String deployMd5 = Hashing.md5().hashBytes(ByteStreams.toByteArray(processDiagram)).toString();
                logger.debug("已部署流程文件md5: {}", deployMd5);
                InputStream originalStream = getClass().getResourceAsStream("/leaveBill.bpmn20.xml");
                if (originalStream == null) {
                    throw new FileNotFoundException("Can't found classpath:/leaveBill.bpmn20.xml");
                }
                String originalMd5 = Hashing.md5().hashBytes(ByteStreams.toByteArray(originalStream)).toString();
                logger.debug("比对流程文件md5: {}", deployMd5);
                if (!deployMd5.equalsIgnoreCase(originalMd5)) {
                    logger.debug("流程文件不一致，重新部署");
                    engine.getRepositoryService().createDeployment().addClasspathResource("leaveBill.bpmn20.xml").deploy();
                } else {
                    logger.debug("流程文件一致，无需更新");
                }
            } else {
                logger.error("流程文件已部署，但无法获取流程文件内容");
                throw new IllegalStateException();
            }
        } else {
            logger.debug("流程文件未部署，直接部署");
            engine.getRepositoryService().createDeployment().addClasspathResource("leaveBill.bpmn20.xml").deploy();
        }
    }

    @Override
    public String flowDefineId() {
        return "leaveBill";
    }

    @Override
    public String flowDefineDesc() {
        return "请假流程";
    }

    @Override
    public String startFlow(String user, Map<String, Object> props) {
        String businessId = doBusinessWork(user, props);
        ProcessInstance instance = engine.getRuntimeService().startProcessInstanceByKey("leaveBill", businessId);
        return instance.getProcessInstanceId();
    }

    private String doBusinessWork(String user, Map<String, Object> props) {
        return null;
    }

    @Override
    public void processTask(String user, String taskId, Map<String, Object> props) {

    }

    @Override
    public void revokeFlow(String user, String flowId) {

    }

    @Override
    public Chicken getDetail(String user, String flowId) {
        return null;
    }

    @Override
    public List<Track> getTrackList(String user, String flowId) {
        return null;
    }

    @Override
    public int getTaskCount(String user) {
        long count = engine.getTaskService().createTaskQuery().taskCandidateOrAssigned(user).count();
        return (int) count;
    }

    @Override
    public List<Task> getTaskList(String user, int first, int limit) {
        List<org.activiti.engine.task.Task> tasks = engine.getTaskService().createTaskQuery().taskCandidateOrAssigned(user).listPage(first, limit);
        List<Task> rst = Lists.transform(tasks, new Function<org.activiti.engine.task.Task, Task>() {
            @Override
            public Task apply(org.activiti.engine.task.Task input) {
                Task task = new Task();
                task.setId(input.getId());
                return task;
            }
        });
        rst.size();
        return rst;
    }

    @Override
    public void stopFlow(String user) {

    }
}
