package com.ruoyi.workflow.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Function;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.constant.OperationType;
import com.ruoyi.workflow.constant.ProcessInstanceStatusConstant;
import com.ruoyi.workflow.constant.TaskCommentConstant;
import com.ruoyi.workflow.factory.FlowServiceFactory;
import com.ruoyi.workflow.model.vo.*;
import com.ruoyi.workflow.service.WorkflowProcessInstanceService;
import com.ruoyi.workflow.utils.ProcessHistoryStatisticTask;
import com.ruoyi.workflow.utils.ProcessVoUtils;
import com.ruoyi.workflow.utils.WorkflowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.task.Comment;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Axel on 2023/6/9 16:45
 *
 * @author Axel
 */
@Service
@Slf4j
public class WorkflowProcessInstanceServiceImpl extends FlowServiceFactory implements WorkflowProcessInstanceService {
    @Resource
    ISysUserService userService;

    @Override
    public Page<ProcessInstanceVo> listPageProcessInstance(Integer current, Integer pageSize) {
        if (ObjectUtils.isEmpty(current)) {
            current = 1;
        }
        if(ObjectUtils.isEmpty(pageSize)){
            pageSize = 20;
        }
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(s);
        List<HistoricProcessInstance> instanceList = historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc()
                .listPage((current-1)*pageSize, pageSize);
        List<ProcessInstanceVo> processInstanceVos = ProcessVoUtils.transformFromProcessInstance(instanceList);
        long count = historicProcessInstanceQuery.count();
        Page<ProcessInstanceVo> page = new Page<>();
//        page.setPages(count/pageSize + 1);
        page.setCurrent(current);
        page.setSize(pageSize);
        page.setRecords(processInstanceVos);
        page.setTotal(count);
        return page;
    }

    @Override
    public Page<ProcessInstanceVo> listPageProcessInstanceByQueryCondition(Integer current, Integer pageSize, String name) {
        if (ObjectUtils.isEmpty(current)) {
            current = 1;
        }
        if(ObjectUtils.isEmpty(pageSize)){
            pageSize = 20;
        }
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .startedBy(s);
        if(StringUtils.isNotBlank(name)){
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().processDefinitionNameLikeIgnoreCase("%" + name + "%").list();
            if(!CollectionUtils.isEmpty(list)){
                List<String> collect = list.stream().map(ProcessDefinition::getKey).collect(Collectors.toList());
                historicProcessInstanceQuery = historicProcessInstanceQuery.processDefinitionKeyIn(collect);
            }
        }
        List<HistoricProcessInstance> instanceList = historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc()
                .listPage((current-1)*pageSize, pageSize);
        List<ProcessInstanceVo> processInstanceVos = ProcessVoUtils.transformFromProcessInstance(instanceList);
        long count = historicProcessInstanceQuery.count();
        Page<ProcessInstanceVo> page = new Page<>();
        page.setCurrent(current);
        page.setSize(pageSize);
        page.setRecords(processInstanceVos);
        page.setTotal(count);
        return page;
    }

    @Override
    public ProcessInstanceDetailVo getProcessInstanceDetailById(String processInstanceId) {
        if(StringUtils.isBlank(processInstanceId)){
            throw new RuntimeException("流程实例id为空，无法进行流程实例详情获取！");
        }
        // 验证当前流程实例是否存在
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Assert.notNull(historicProcessInstance, "当前流程实例不存在，无法获取实例详情！");
        Date endTime = historicProcessInstance.getEndTime();
        List<Task> list1 = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        //获取流程定义xml图
        String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
        String xmlData = WorkflowUtils.getXmlData(processDefinitionId);
        // 获取当前历史的任务节点，已流转节点
        List<String> historicTaskNodes = WorkflowUtils.getHistoricTaskNodes(processInstanceId);
        // 获取流程定义的所有流程任务
        List<UserTask> userTasks = WorkflowUtils.listAllUserTaskOfProcessDefinition(processDefinitionId);
        // 获取所有的历程评论信息
        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
        // 流程图完成状态数据获取
//        Map<String, Integer> historyNodeMap = historicTaskNodes.stream().collect(Collectors.toMap(o -> o, o -> 1));
//        List<String> collect = userTasks.stream().map(UserTask::getId).collect(Collectors.toList());
//        ArrayList<ProcessNodeVo> processNodeVos = new ArrayList<>(collect.size());
//        collect.forEach(s -> {
//            Integer integer = historyNodeMap.get(s);
//            ProcessNodeVo processNodeVo = new ProcessNodeVo();
//            processNodeVo.setKey(s);
//            if(!ObjectUtils.isEmpty(integer)){
//                processNodeVo.setCompleted(true);
//            }else{
//                processNodeVo.setCompleted(false);
//            }
//            processNodeVos.add(processNodeVo);
//        });
        List<ProcessNodeVo> processNodeVos = WorkflowUtils.getHistoricActivityNodes(processInstanceId);
        // 获取历史任务，并组合comment
        List<HistoricTaskInstance> taskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
//                .finished()
                .orderByHistoricTaskInstanceStartTime().asc().list();
        // 用户信息更换
        Set<String> userSet = taskInstances.stream().map(HistoricTaskInstance::getAssignee).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        List<SysUser> userList = userService.listUserByIdList(userSet);
        Map<Long, String> map = userList.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
        //comment组装
        ArrayList<ProcessHistoryVo> list = new ArrayList<>(taskInstances.size());
        HashMap<String, List<Comment>> commentMap = new HashMap<>(taskInstances.size());
        processInstanceComments.forEach(comment -> {
            String taskId = comment.getTaskId();
            List<Comment> comments = commentMap.get(taskId);
            if(CollectionUtils.isEmpty(comments)){
                ArrayList<Comment> arrayList = new ArrayList<>();
                arrayList.add(comment);
                commentMap.put(taskId, arrayList);
            }else{
                comments.add(comment);
            }
        });
        taskInstances.forEach(historicTaskInstance -> {
            String id = historicTaskInstance.getId();
            Date taskEndTime = historicTaskInstance.getEndTime();
            ProcessHistoryVo processHistoryVo = new ProcessHistoryVo();
            if(ObjectUtils.isNotEmpty(taskEndTime)){
                List<Comment> comments = commentMap.get(id);
                if (!CollectionUtils.isEmpty(comments) && comments.size()==1) {
                    Comment comment = comments.get(0);
                    if(ObjectUtils.isNotEmpty(comment)){
                        String type = comment.getType();
                        String fullMessage = comment.getFullMessage();
                        String taskId = comment.getTaskId();
                        if(StringUtils.equals(type, OperationType.ASSIGNEETASK)){
                            String assignee = historicTaskInstance.getAssignee();
                            String s = map.get(Long.parseLong(assignee));
                            processHistoryVo.setTaskId(taskId);
                            processHistoryVo.setOperationType(OperationType.ASSIGNEETASK);
                            processHistoryVo.setAssignee(assignee);
                            processHistoryVo.setComment(fullMessage);
                            processHistoryVo.setDuration(ProcessVoUtils.getDate(historicTaskInstance.getDurationInMillis()));
                            processHistoryVo.setAssigneeName(s);
                            processHistoryVo.setReceiveTime(historicTaskInstance.getCreateTime());
                            processHistoryVo.setCompleteTime(taskEndTime);
                            processHistoryVo.setNodeName(historicTaskInstance.getName());
                        }
                    }
                }else{
                    String assignee = historicTaskInstance.getAssignee();
                    String s = map.get(Long.parseLong(assignee));
                    processHistoryVo.setAssignee(assignee);
                    processHistoryVo.setAssigneeName(s);
                    processHistoryVo.setDuration(ProcessVoUtils.getDate(historicTaskInstance.getDurationInMillis()));
                    processHistoryVo.setTaskId(historicTaskInstance.getId());
                    processHistoryVo.setReceiveTime(historicTaskInstance.getCreateTime());
                    processHistoryVo.setCompleteTime(taskEndTime);
                    processHistoryVo.setNodeName(historicTaskInstance.getName());
                    if(!CollectionUtils.isEmpty(comments)){
                        comments.forEach(comment -> {
                            String type = comment.getType();
                            if(StringUtils.equals(type, TaskCommentConstant.TASKOPERATION)){
                                processHistoryVo.setOperationType(comment.getFullMessage());
                            }
                            if(StringUtils.equals(type, TaskCommentConstant.TASKCOMMENT)){
                                processHistoryVo.setComment(comment.getFullMessage());
                            }
                        });
                    }
                }
            }else {
                String assignee = historicTaskInstance.getAssignee();
                String s = map.get(Long.parseLong(assignee));
                processHistoryVo.setAssignee(assignee);
                processHistoryVo.setAssigneeName(s);
                long time = DateUtils.getNowDate().getTime();
                Date createTime = historicTaskInstance.getCreateTime();
                long time1 = createTime.getTime();
                processHistoryVo.setDuration(ProcessVoUtils.getDate(time - time1));
                processHistoryVo.setTaskId(historicTaskInstance.getId());
                processHistoryVo.setReceiveTime(createTime);
                processHistoryVo.setCompleteTime(taskEndTime);
                processHistoryVo.setNodeName(historicTaskInstance.getName());
                processHistoryVo.setOperationType("running");
            }
            list.add(processHistoryVo);
        });
        ProcessInstanceDetailVo processInstanceDetailVo = new ProcessInstanceDetailVo();
        processInstanceDetailVo.setProcessInstanceId(processInstanceId);
        processInstanceDetailVo.setProcessDefinitionId(processDefinitionId);
        processInstanceDetailVo.setXmlData(xmlData);
        processInstanceDetailVo.setHistoryList(list);
        processInstanceDetailVo.setNodes(processNodeVos);
        processInstanceDetailVo.setProcessKey(historicProcessInstance.getProcessDefinitionKey());
        processInstanceDetailVo.setIsStartUserTask(false);
        if(ObjectUtils.isNotEmpty(endTime)){
            processInstanceDetailVo.setProcessStatus(ProcessInstanceStatusConstant.FINISHED);
        }else{
            processInstanceDetailVo.setProcessStatus(ProcessInstanceStatusConstant.RUNNING);
        }
        if(!CollectionUtils.isEmpty(list1)){
            String collect1 = list1.stream().map(Task::getId).collect(Collectors.joining(","));
            String collect = list1.stream().map(Task::getTaskDefinitionKey).collect(Collectors.joining(","));
            processInstanceDetailVo.setActiveTaskId(collect1);
            processInstanceDetailVo.setActiveTaskKey(collect);
            UserTask firstUserTaskOfProcess = WorkflowUtils.getFirstUserTaskOfProcess(processDefinitionId);
            String id = firstUserTaskOfProcess.getId();
            boolean equals = StringUtils.equals(id,  collect);
            processInstanceDetailVo.setIsStartUserTask(equals);
        }
        return processInstanceDetailVo;
    }

    @Override
    public List<RejecttableTaskVo> listRejecttableTaskOfProcess(String processInstanceId) {
        if(StringUtils.isBlank(processInstanceId)){
            throw new RuntimeException("流程实例id为空，无法获取正确的可驳回节点，请刷新页面后重试！");
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Assert.notNull(historicProcessInstance, "当前流程实例不存在，无法查询流程可驳回节点数据，请刷新页面后重试！！");
        List<UserTask> historicUserTaskNodeByProcessInstanceId = WorkflowUtils.getHistoricUserTaskNodeByProcessInstanceId(processInstanceId);
        return historicUserTaskNodeByProcessInstanceId.stream().map((Function<UserTask, RejecttableTaskVo>) input -> {
            RejecttableTaskVo rejecttableTaskVo = new RejecttableTaskVo();
            rejecttableTaskVo.setTaskKey(input.getId());
            rejecttableTaskVo.setTaskName(input.getName());
            rejecttableTaskVo.setProcessInstanceId(processInstanceId);
            return rejecttableTaskVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProcessStatisticVo> listProcessStatistic() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().latestVersion().list();
        if(!CollectionUtils.isEmpty(list)){
            SecurityContext context = SecurityContextHolder.getContext();
            ForkJoinPool forkJoinPool = new ForkJoinPool(list.size());
            ArrayList<ProcessStatisticVo> processStatisticVos = new ArrayList<>(list.size());
            list.forEach(processDefinition -> {
                ProcessHistoryStatisticTask task = new ProcessHistoryStatisticTask(context, processDefinition);
                ForkJoinTask<ProcessStatisticVo> submit = forkJoinPool.submit(task);
                try {
                    ProcessStatisticVo processStatisticVo = submit.get();
                    processStatisticVos.add(processStatisticVo);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("流程统计信息线程异常中断！");
                    throw new RuntimeException("流程统计信息线程异常中断！");
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    log.error("流程统计信息线程异常中断！");
                    throw new RuntimeException("流程统计信息线程执行异常！");
                }
            });
            return processStatisticVos;
        }
        return null;
    }
}
