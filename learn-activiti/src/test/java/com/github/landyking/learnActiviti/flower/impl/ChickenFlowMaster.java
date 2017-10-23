package com.github.landyking.learnActiviti.flower.impl;

import com.github.landyking.learnActiviti.flower.FlowConstans;
import com.github.landyking.learnActiviti.flower.FlowMaster;
import com.github.landyking.learnActiviti.flower.Task;
import com.github.landyking.learnActiviti.flower.Track;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 16:19
 * note:
 */
public class ChickenFlowMaster implements FlowMaster<Chicken> {
    public static final String LEAVE_BILL = "leaveBill";
    public static final String LEAVE_BILL_CANDIDATE_BPMN20_XML = "leaveBillCandidate.bpmn20.xml";
    private Logger logger = LoggerFactory.getLogger(ChickenFlowMaster.class);
    private ProcessEngine engine;
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProcessEngine getEngine() {
        return engine;
    }

    public void setEngine(ProcessEngine engine) {
        this.engine = engine;
    }

    public void init() throws IOException {
        logger.debug("开始自动更新流程文件");
        ProcessDefinition leaveBill = engine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey(LEAVE_BILL).latestVersion().singleResult();
        if (leaveBill != null) {
            InputStream processDiagram = engine.getRepositoryService().getProcessModel(leaveBill.getId());
            if (processDiagram != null) {
                String deployMd5 = Hashing.md5().hashBytes(ByteStreams.toByteArray(processDiagram)).toString();
                logger.debug("已部署流程文件md5: {}", deployMd5);
                InputStream originalStream = getClass().getResourceAsStream("/" + LEAVE_BILL_CANDIDATE_BPMN20_XML);
                if (originalStream == null) {
                    throw new FileNotFoundException("Can't found classpath:/" + LEAVE_BILL_CANDIDATE_BPMN20_XML);
                }
                String originalMd5 = Hashing.md5().hashBytes(ByteStreams.toByteArray(originalStream)).toString();
                logger.debug("比对流程文件md5: {}", deployMd5);
                if (!deployMd5.equalsIgnoreCase(originalMd5)) {
                    logger.debug("流程文件不一致，重新部署");
                    engine.getRepositoryService().createDeployment().addClasspathResource(LEAVE_BILL_CANDIDATE_BPMN20_XML).deploy();
                } else {
                    logger.debug("流程文件一致，无需更新");
                }
            } else {
                logger.error("流程文件已部署，但无法获取流程文件内容");
                throw new IllegalStateException();
            }
        } else {
            logger.debug("流程文件未部署，直接部署");
            engine.getRepositoryService().createDeployment().addClasspathResource(LEAVE_BILL_CANDIDATE_BPMN20_XML).deploy();
        }
    }

    @Override
    public String flowDefineId() {
        return LEAVE_BILL;
    }

    @Override
    public String flowDefineDesc() {
        return "请假流程";
    }

    @Override
    public String startFlow(String user, Map<String, Object> props) {
        String businessId = doBusinessWork(user, props);
        engine.getIdentityService().setAuthenticatedUserId(user);
        ProcessInstance instance = engine.getRuntimeService().startProcessInstanceByKey(LEAVE_BILL, businessId, props);
        String processInstanceId = instance.getProcessInstanceId();
        int update = jdbcTemplate.update("update t_data set processInstanceId=? where id=?", processInstanceId, businessId);
        Assert.isTrue(update == 1);
        return processInstanceId;
    }

    private String doBusinessWork(String user, Map<String, Object> props) {
        Integer count = (Integer) props.get("count");
        String name = (String) props.get("name");

        Assert.notNull(count, "count can't empty");
        Assert.hasText(name, "name can't empty");
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        int update = jdbcTemplate.update("insert into t_data (id,count,name,status,endFlag) values(?,?,?,?,?)"
                , id, count, name, FlowConstans.status_processing, FlowConstans.end_false);
        Assert.isTrue(update == 1);
        return id;
    }

    @Override
    public void processTask(String user, String taskId, Map<String, Object> props) {
        TaskQuery taskQuery = engine.getTaskService().createTaskQuery();
        if (StringUtils.hasText(user)) {
            taskQuery = taskQuery.taskCandidateOrAssigned(user);
        }
        org.activiti.engine.task.Task task = taskQuery
                .processDefinitionKey(LEAVE_BILL).taskId(taskId).singleResult();
        if (task == null) {
            return;
        }
        if (!StringUtils.hasText(task.getAssignee())) {
            engine.getTaskService().claim(taskId, user);
        }
        engine.getTaskService().complete(taskId);
    }

    @Override
    public void revokeFlow(String user, String flowId) {

    }

    @Override
    public Chicken getDetail(String user, String flowId) {
        ProcessInstance instance = engine.getRuntimeService().createProcessInstanceQuery().processInstanceId(flowId).singleResult();
        String businessKey = instance.getBusinessKey();
        return null;
    }

    @Override
    public List<Track> getTrackList(String user, String flowId) {
        return null;
    }

    @Override
    public long getTaskCount(String user) {
        TaskQuery taskQuery = engine.getTaskService().createTaskQuery().processDefinitionKey(LEAVE_BILL);
        if (StringUtils.hasText(user)) {
            taskQuery = taskQuery.taskCandidateOrAssigned(user);
        }
        long count = taskQuery.count();
        return count;
    }

    @Override
    public List<Task> getTaskList(String user, int first, int limit) {
        TaskQuery taskQuery = engine.getTaskService().createTaskQuery().processDefinitionKey(LEAVE_BILL);
        if (StringUtils.hasText(user)) {
            taskQuery = taskQuery.taskCandidateOrAssigned(user);
        }
        List<org.activiti.engine.task.Task> tasks = taskQuery.listPage(first, limit);
        List<Task> rst = Lists.transform(tasks, new Function<org.activiti.engine.task.Task, Task>() {
            @Override
            public Task apply(org.activiti.engine.task.Task input) {
                Task task = new Task();
                task.setId(input.getId());
                task.setName(input.getName());
                task.setStartTime(input.getCreateTime());
                task.setProcessInstanceId(input.getProcessInstanceId());
                ProcessInstance processInstance = engine.getRuntimeService().createProcessInstanceQuery().processInstanceId(input.getProcessInstanceId()).singleResult();
                if (processInstance != null) {
                    task.setFlowId(processInstance.getBusinessKey());
                }
                String user = input.getAssignee();
                if (!StringUtils.hasText(user)) {
                    user = input.getOwner();
                }
                task.setUser(user);
                if (!StringUtils.hasText(user)) {
                    task.setCandidate(getTaskCandidate(input.getId()));
                }
                return task;
            }
        });
        rst.size();
        return rst;
    }

    private Set<String> getTaskCandidate(String taskId) {
        Set<String> users = new HashSet<String>();
        List<IdentityLink> identityLinkList = engine.getTaskService().getIdentityLinksForTask(taskId);
        if (identityLinkList != null && identityLinkList.size() > 0) {
            for (Iterator iterator = identityLinkList.iterator(); iterator
                    .hasNext(); ) {
                IdentityLink identityLink = (IdentityLink) iterator.next();
                if (identityLink.getType().equals("candidate")) {
                    if (identityLink.getUserId() != null) {
                        users.add(identityLink.getUserId());
                    }
                    if (identityLink.getGroupId() != null) {
                        // 根据组获得对应人员
                        List<User> userList = engine.getIdentityService().createUserQuery()
                                .memberOfGroup(identityLink.getGroupId()).list();
                        if (userList != null && userList.size() > 0) {
                            for (User user : userList) {
                                users.add(user.getId());
                            }
                        }
                    }
                }
            }

        }
        return users;
    }

    @Override
    public void stopFlow(String user, String flowId) {
        String processInstanceId = jdbcTemplate.queryForObject("select processInstanceId from t_data where id=?", String.class, flowId);
        deleteProcessInstance(processInstanceId);
        int update = jdbcTemplate.update("update t_data set status=?,endFlag=? where id=? and endFlag=? and status=?",
                FlowConstans.end_true, FlowConstans.status_stoped, flowId, FlowConstans.end_false, FlowConstans.status_processing);
        Assert.isTrue(update == 1);
    }

    protected void deleteProcessInstance(String processInstanceId) {
        if (StringUtils.hasText(processInstanceId)) {
            engine.getRuntimeService().deleteProcessInstance(processInstanceId, "stop");
        }
    }

    @Override
    public void stopTask(String user, String taskId) {

    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    private TaskEntity findTaskById(String taskId) throws Exception {
        TaskEntity task = (TaskEntity) engine.getTaskService().createTaskQuery().taskId(
                taskId).singleResult();
        if (task == null) {
            throw new Exception("任务实例未找到!");
        }
        return task;
    }

    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(
            String taskId) throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) engine.getRepositoryService())
                .getDeployedProcessDefinition(findTaskById(taskId)
                        .getProcessDefinitionId());

        if (processDefinition == null) {
            throw new Exception("流程定义未找到!");
        }

        return processDefinition;
    }

    private ActivityImpl findActivitiImpl(String taskId, String activityId)
            throws Exception {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

        // 获取当前活动节点ID
        if (!StringUtils.hasText(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();
        }

        // 根据流程定义，获取该流程实例的结束节点
        if (activityId.toUpperCase().equals("END")) {
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {
                List<PvmTransition> pvmTransitionList = activityImpl
                        .getOutgoingTransitions();
                if (pvmTransitionList.isEmpty()) {
                    return activityImpl;
                }
            }
        }

        // 根据节点ID，获取对应的活动节点
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)
                .findActivity(activityId);

        return activityImpl;
    }

    public long getUnfinishProcessCount() {
        long count = engine.getRuntimeService().createProcessInstanceQuery().processDefinitionKey(LEAVE_BILL).count();
        return count;
    }
}
