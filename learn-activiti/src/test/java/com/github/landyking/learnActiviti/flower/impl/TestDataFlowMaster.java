package com.github.landyking.learnActiviti.flower.impl;

import com.github.landyking.learnActiviti.flower.*;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/10/20 16:19
 * note:
 */
public class TestDataFlowMaster implements FlowMaster<TestData>, FlowerCallback {
    public static final String LEAVE_BILL = "leaveBill";
    public static final String LEAVE_BILL_CANDIDATE_BPMN20_XML = "leaveBillCandidate.bpmn20.xml";
    public static final RowMapper<TestData> TEST_DATA_ROW_MAPPER = new RowMapper<TestData>() {
        @Override
        public TestData mapRow(ResultSet rs, int rowNum) throws SQLException {
            TestData testData = new TestData();
            testData.setCount(rs.getInt("count"));
            testData.setEndFlag(rs.getInt("endFlag"));
            testData.setEndTime(rs.getTimestamp("endTime"));
            testData.setStartTime(rs.getTimestamp("startTime"));
            testData.setId(rs.getString("id"));
            testData.setName(rs.getString("name"));
            testData.setStatus(rs.getInt("status"));
            testData.setProcessInstanceId(rs.getString("processInstanceId"));
            testData.setStarter(rs.getString("starter"));
            return testData;
        }
    };
    private Logger logger = LoggerFactory.getLogger(TestDataFlowMaster.class);
    private ProcessEngine engine;
    private JdbcTemplate jdbcTemplate;
    private TransactionTemplate transactionTemplate;

    public TransactionTemplate getTransactionTemplate() {
        return transactionTemplate;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

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
    public String startFlow(final String user, final Map<String, Object> props) {
        return transactionTemplate.execute(new TransactionCallback<String>() {
            @Override
            public String doInTransaction(TransactionStatus status) {
                String businessId = doBusinessWork(user, props);
                engine.getIdentityService().setAuthenticatedUserId(user);
                props.put("flowId", businessId);
                ProcessInstance instance = engine.getRuntimeService().startProcessInstanceByKey(LEAVE_BILL, businessId, props);
                String processInstanceId = instance.getProcessInstanceId();
                int update = jdbcTemplate.update("update t_data set processInstanceId=? where id=?", processInstanceId, businessId);
                Assert.isTrue(update == 1);
                return processInstanceId;
            }
        });

    }

    private String doBusinessWork(String user, Map<String, Object> props) {
        Integer count = (Integer) props.get("count");
        String name = (String) props.get("name");

        Assert.notNull(count, "count can't empty");
        Assert.hasText(name, "name can't empty");
        String id = Ids.newID();
        int update = jdbcTemplate.update("insert into t_data (id,count,name,status,endFlag,startTime,starter) values(?,?,?,?,?,?,?)"
                , id, count, name, FlowConstans.status_processing, FlowConstans.end_false, new Date(), user);
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
    public List<Track> getTrackList(String flowId) {
        return jdbcTemplate.query("select * from t_track where flowId=? order by startTime desc", new RowMapper<Track>() {
            @Override
            public Track mapRow(ResultSet rs, int rowNum) throws SQLException {
                Track t = new Track();
                t.setId(rs.getString("id"));
                t.setEndTime(rs.getTimestamp("endTime"));
                t.setStartTime(rs.getTimestamp("startTime"));
                t.setFlowId(rs.getString("flowId"));
                t.setOperate(rs.getInt("operate"));
                t.setOpinion(rs.getString("opinion"));
                t.setTitle(rs.getString("title"));
                t.setType(rs.getInt("type"));
                t.setUserId(rs.getString("userId"));
                return t;
            }
        }, flowId);
    }

    @Override
    public long getTaskCount(String user, long timestamp) {
        TaskQuery taskQuery = engine.getTaskService().createTaskQuery().processDefinitionKey(LEAVE_BILL);
        if (StringUtils.hasText(user)) {
            taskQuery = taskQuery.taskCandidateOrAssigned(user);
        }
        if (timestamp > 0) {
            taskQuery = taskQuery.taskCreatedBefore(new Date(timestamp));
        }
        long count = taskQuery.count();
        return count;
    }

    @Override
    public List<Task> getTaskList(final String user, long timestamp, int first, int limit) {
        TaskQuery taskQuery = engine.getTaskService().createTaskQuery().processDefinitionKey(LEAVE_BILL);
        if (StringUtils.hasText(user)) {
            taskQuery = taskQuery.taskCandidateOrAssigned(user);
        }
        if (timestamp > 0) {
            taskQuery = taskQuery.taskCreatedBefore(new Date(timestamp));
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
                if (StringUtils.hasText(user)) {
                    //通过user查询，直接设置该user
                    task.setUser(user);
                } else {
                    //查询所有，需要提取对应的处理人
                    String tmpUser = input.getAssignee();
                    if (!StringUtils.hasText(tmpUser)) {
                        tmpUser = input.getOwner();
                    }
                    task.setUser(tmpUser);
                    if (!StringUtils.hasText(tmpUser)) {
                        task.setCandidate(getTaskCandidate(input.getId()));
                    }
                }
                return task;
            }
        });
        rst.size();
        return rst;
    }

    @Override
    public List<Tuple<Task, TestData>> getTaskWithBusinessDataList(String user, long timestamp, int first, int limit) {
        List<Tuple<Task, TestData>> rst = Lists.newArrayList();
        while (true) {
            rst.clear();
            List<Task> taskList = getTaskList(user, timestamp, first, limit);
            for (Task one : taskList) {
                TestData detail = getBusinessData(one.getFlowId());
                if (detail == null) {
                    //自动清理不存在的流程数据
                    engine.getRuntimeService().deleteProcessInstance(one.getProcessInstanceId(), "auto clean");
                    //重新加载数据
                    continue;
                } else {
                    rst.add(Tuple.newInstance(one, detail));
                }
            }
            break;
        }
        return rst;
    }

    public TestData getBusinessData(String flowId) {
        try {
            return jdbcTemplate.queryForObject("select * from t_data where id=?", TEST_DATA_ROW_MAPPER, flowId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public long getBusinessDataCount(String user, long position, Object... others) {
        StringBuilder sb = new StringBuilder("select count(*) from t_data where 1=1");
        List<Object> params = new ArrayList<Object>();
        if (StringUtils.hasText(user)) {
            sb.append(" and starter=？");
            params.add(user);
        }
        if (position > 0) {
            sb.append(" and startTime<?");
            params.add(new Date(position));
        }
        //TODO 租户过滤
        return jdbcTemplate.queryForObject(sb.toString(), params.toArray(), Number.class).longValue();
    }

    @Override
    public long getJoinBusinessDataCount(String user, long position, boolean includeStarter, Object... others) {
        return 0;
    }

    @Override
    public List<TestData> getBusinessDataList(String user, long position, int first, int limit, Object... others) {
        StringBuilder sb = new StringBuilder("select * from t_data where 1=1");
        List<Object> params = new ArrayList<Object>();
        if (StringUtils.hasText(user)) {
            sb.append(" and starter=？");
            params.add(user);
        }
        if (position > 0) {
            sb.append(" and startTime<?");
            params.add(new Date(position));
        }
        //TODO 租户过滤
        sb.append(" order by startTime desc limit ?,?");
        params.add(first);
        params.add(limit);
        return jdbcTemplate.query(sb.toString(), params.toArray(), TEST_DATA_ROW_MAPPER);
    }

    @Override
    public List<TestData> getJoinBusinessDataList(String user, long position, boolean includeStarter, int first, int limit, Object... others) {
        return null;
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
        String processInstanceId = null;
        List<String> rstList = jdbcTemplate.queryForList("select processInstanceId from t_data where id=?", String.class, flowId);
        if (!rstList.isEmpty()) {
            processInstanceId = rstList.get(0);
        }
        if (!StringUtils.hasText(processInstanceId)) {
            throw new IllegalArgumentException("Can't found flowId: " + flowId);
        }
        if (engine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).count() == 1) {
            //如果流程数据存在，则删除流程数据，自动触发业务数据状态更新。
            deleteProcessInstance(user, processInstanceId);
        } else {
            //如果流程数据不存在，直接更新业务数据状态。
            updateBusinessDataStopAndAddTrack(FlowConstans.status_stoped, flowId);
        }

    }

    private void updateBusinessDataStopAndAddTrack(int status, String processBusinessKey) {
        Date now = new Date();
        int update = jdbcTemplate.update("update t_data set status=?,endFlag=?,endTime=? where id=? and endFlag=? and status=?",
                status, FlowConstans.end_true, now, processBusinessKey, FlowConstans.end_false, FlowConstans.status_processing);
        String operateDesc = "";
        if (status == FlowConstans.status_stoped) {
            operateDesc = "中止";
        } else if (status == FlowConstans.status_success) {
            operateDesc = "结束";
        }
        Assert.isTrue(update == 1, operateDesc + "流程" + processBusinessKey + "失败");
        addTrack(processBusinessKey, FlowConstans.operate_pass, Authentication.getAuthenticatedUserId(), now, now, null, operateDesc, FlowConstans.trackType_end);
    }

    protected void deleteProcessInstance(String user, String processInstanceId) {
        if (StringUtils.hasText(processInstanceId)) {

            //存在时才清理
            Assert.hasText(user, "user can't empty");
            engine.getIdentityService().setAuthenticatedUserId(user);
            engine.getRuntimeService().deleteProcessInstance(processInstanceId, "stop");
        }

    }

    @Override
    public void onExecutionEnd(Object execution) {
        ExecutionEntity exec = (ExecutionEntity) execution;
        String deleteReason = exec.getDeleteReason();
        String processBusinessKey = exec.getProcessBusinessKey();
        if (existBusinessKey(processBusinessKey)) {

            if (StringUtils.hasText(deleteReason)) {
                if ("stop".equals(deleteReason)) {
                    //中止
                    updateBusinessDataStopAndAddTrack(FlowConstans.status_stoped, processBusinessKey);
                    return;
                }
            } else {
                //正常结束
                updateBusinessDataStopAndAddTrack(FlowConstans.status_success, processBusinessKey);
                return;
            }
        }
    }

    private boolean existBusinessKey(String processBusinessKey) {
        if (StringUtils.hasText(processBusinessKey)) {
            return jdbcTemplate.queryForObject("select count(1) from t_data where id=?", Number.class, processBusinessKey).intValue() == 1;
        }
        return false;
    }

    @Override
    public void onExecutionStart(Object execution) {
        ExecutionEntity exec = (ExecutionEntity) execution;
        Assert.notNull(exec.getVariable("flowId"), "flowId can't empty");
        Date now = new Date();
        addTrack(exec.getProcessBusinessKey(), FlowConstans.operate_pass, Authentication.getAuthenticatedUserId(), now, now, null, "启动", FlowConstans.trackType_start);
    }

    @Override
    public void onTaskCreate(Object delegateTask) {

    }

    @Override
    public void onTaskAssignment(Object delegateTask) {

    }

    @Override
    public void onTaskComplete(Object delegateTask) {
        DelegateTask task = (DelegateTask) delegateTask;
        String flowId = (String) task.getVariable("flowId");
        Assert.notNull(flowId, "flowId can't empty");
        Integer operate = task.getVariableLocal("operate", Integer.class);
        if (operate == null) {
            operate = FlowConstans.operate_pass;
        }
        String userId = task.getAssignee();
        Date startTime = task.getCreateTime();
        Date endTime = new Date();
        String opinion = (String) task.getVariable("opinion");
        String name = task.getName();
        addTrack(flowId, operate, userId, startTime, endTime, opinion, name, FlowConstans.trackType_task);
    }

    private void addTrack(String flowId, Integer operate, String userId, Date startTime, Date endTime, String opinion, String name, int type) {
        if (!StringUtils.hasText(userId)) {
            userId = "0";
        }
        int rows = jdbcTemplate.update("insert into t_track (id,userId,startTime,endTime,opinion,operate,title,flowId,type)" +
                        "values (?,?,?,?,?,?,?,?,?)", Ids.newID(), userId, startTime, endTime, opinion,
                operate, name, flowId, type);
        Assert.isTrue(rows == 1);
    }
}
