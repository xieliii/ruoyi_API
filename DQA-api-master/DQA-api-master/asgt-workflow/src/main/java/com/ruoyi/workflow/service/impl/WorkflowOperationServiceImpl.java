package com.ruoyi.workflow.service.impl;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.SnowFlakeUtil;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.workflow.adapter.AbstractBusinessOperationAdapter;
import com.ruoyi.workflow.config.BusinessWorkflowConfig;
import com.ruoyi.workflow.constant.OperationType;
import com.ruoyi.workflow.constant.TaskCommentConstant;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.factory.FlowServiceFactory;
import com.ruoyi.workflow.listener.BusinessWorkflowLifeCycleListener;
import com.ruoyi.workflow.model.dto.*;
import com.ruoyi.workflow.model.vo.ProcessStartVo;
import com.ruoyi.workflow.model.vo.TaskCompleteVo;
import com.ruoyi.workflow.service.WorkflowAssigneeService;
import com.ruoyi.workflow.service.WorkflowOperationService;
import com.ruoyi.workflow.utils.WorkflowUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Axel on 2023/6/7 11:54
 *
 * @author Axel
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowOperationServiceImpl extends FlowServiceFactory implements WorkflowOperationService {
    @Resource
    private BusinessWorkflowConfig businessWorkflowConfig;
    @Resource
    private WorkflowAssigneeService assigneeService;

    @Override
    public <T> ProcessStartVo<T> startProcess(ProcessStartDto<T> processStartDto) {
        Assert.notNull(processStartDto, "流程启动数据为空，无法正确发起流程!");
        String businessKey = processStartDto.getBusinessKey();
        T businessData = processStartDto.getBusinessData();
        Assert.notBlank(businessKey, "流程业务键为空，无法正确发起流程！");
        Assert.notNull(businessData, "流程业务数据为空，无法正确发起流程！");
        // 认证用户身份并启动流程，并通过第一个流程任务节点
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(businessKey).latestVersion().singleResult();
        Assert.notNull(processDefinition, "当前流程不存在，无法启动流程！");
        identityService.setAuthenticatedUserId(s);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(businessKey);
        Assert.notNull(processInstance, "流程启动失败，请重试！");
        String processInstanceId = processInstance.getProcessInstanceId();
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        Assert.notEmpty(list, "当前流程启动失败，或者定义异常，请联系管理员解决！");
        if(list.size()>1){
            throw new RuntimeException("当前流程定义异常，请联系管理员解决！");
        }
        Task task = list.get(0);
        Assert.notNull(task, "当前流程启动失败，请重试！");
        String id = task.getId();
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(businessKey);
        // 设置流程处理人
        List<UserTask> userTasks = WorkflowUtils.listAllUserTaskOfProcessDefinition(processDefinition.getId());
        List userList = businessOperationAdapter.saveWorkflowAssignee(userTasks);
        // 添加流程实例id
        for (Object o : userList) {
            AgWorkflowAssigneeEntity o1 = (AgWorkflowAssigneeEntity) o;
            o1.setProcessInstanceId(processInstanceId);
            o1.setId(SnowFlakeUtil.snowFlakeId());
        }
        assigneeService.saveBatch(userList);
        // 添加流程启动意见，并完成第一个节点
        taskService.setAssignee(id, s);
        taskService.addComment(id, processInstanceId, TaskCommentConstant.TASKOPERATION, OperationType.STARTPROCESS);
        taskService.addComment(id, processInstanceId, TaskCommentConstant.TASKMESSAGE, TaskCommentConstant.STARTPROCESSMESSAGE);
        taskService.addComment(id, processInstanceId, TaskCommentConstant.TASKCOMMENT, TaskCommentConstant.STARTPROCESSMESSAGE);
        taskService.complete(id);
        BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(businessKey);
        T t = null;
        if(ObjectUtils.isNotEmpty(businessWorkflowService)){
            ProcessStartListenerDto<T> processStartListenerDto = new ProcessStartListenerDto<>();
            processStartListenerDto.setProcessInstanceId(processInstanceId);
            processStartListenerDto.setBusinessData(businessData);
            // 调用声明周期钩子，执行业务逻辑
            t = businessWorkflowService.onProcessStart(processStartListenerDto);
        }
        if(ObjectUtils.isEmpty(t)){
            t = businessData;
        }
        ProcessStartVo<T> tProcessStartVo = new ProcessStartVo<>();
        tProcessStartVo.setBusinessData(t);
        tProcessStartVo.setProcessKey(businessKey);
        tProcessStartVo.setProcessInstanceId(processInstanceId);
        return tProcessStartVo;
    }


    @Override
    public <T> TaskCompleteVo<T> rejectProcess(TaskRejectDto<T> taskRejectDto) {
        Assert.notNull(taskRejectDto, "流程操作数据为空，无法执行流程操作，请刷新页面重试！");
        String taskId = taskRejectDto.getTaskId();
        Assert.notBlank(taskId, "当前流程任务id为空，无法进行流程处理！");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在或者已被处理，请刷新页面重试！");
        String processDefinitionId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        String processInstanceId = task.getProcessInstanceId();
        Assert.notBlank(taskId, "流程任务id为空，无法进行流程操作！");
        String rejectTaskKey = taskRejectDto.getRejectTaskKey();
        Assert.notBlank(rejectTaskKey, "流程驳回节点为空，无法进行驳回操作！");
        // 此处未做精确判断，精确判断应该查询所有的任务并进行一一判断
        Map<String, String> userTaskMapOfProcess = WorkflowUtils.getUserTaskMapOfProcess(processDefinitionId);
        Assert.notEmpty(userTaskMapOfProcess, "当前流程定义错误，请联系管理员解决！");
        String s = userTaskMapOfProcess.get(rejectTaskKey);
        if(StringUtils.isBlank(s)){
            throw new RuntimeException("当前流程不存在驳回节点，或者流程定义错误，请刷新页面重试！");
        }
        String s1 = userTaskMapOfProcess.get(taskDefinitionKey);
        Assert.notBlank(s1, "当前流程定义错误，请联系管理员解决！");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        Assert.notNull(processDefinition, "流程定义不存在，无法进行流程操作，请联系管理员解决！");
        String key = processDefinition.getKey();
        // 为当前任务添加comment
        String comment = taskRejectDto.getComment();
        T businessData = taskRejectDto.getBusinessData();
        comment = StringUtils.isBlank(comment)?TaskCommentConstant.REJECTTASKMESSAGE:comment;
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKOPERATION, OperationType.REJECTTASK);
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKMESSAGE, TaskCommentConstant.REJECTTASKMESSAGE);
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKCOMMENT, comment);
        // 执行驳回操作
        runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId).moveActivityIdTo(taskDefinitionKey, rejectTaskKey).changeState();
        // 执行生命周期函数
        BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(key);
        T t = null;
        if(ObjectUtils.isNotEmpty(businessWorkflowService)){
            TaskRejectListenerDto<T> tTaskRejectListenerDto = new TaskRejectListenerDto<>();
            tTaskRejectListenerDto.setRejectTaskKey(rejectTaskKey);
            tTaskRejectListenerDto.setRejectTaskName(s);
            tTaskRejectListenerDto.setBusinessData(businessData);
            tTaskRejectListenerDto.setTaskId(taskId);
            tTaskRejectListenerDto.setProcessKey(key);
            tTaskRejectListenerDto.setTaskName(s1);
            tTaskRejectListenerDto.setProcessInstanceId(processInstanceId);
            t = businessWorkflowService.onProcessReject(tTaskRejectListenerDto);
        }
        // 返回数据
        if(ObjectUtils.isEmpty(t)){
            t = businessData;
        }
        TaskCompleteVo<T> tTaskCompleteVo = new TaskCompleteVo<>();
        tTaskCompleteVo.setBusinessData(t);
        tTaskCompleteVo.setTaskId(taskId);
        tTaskCompleteVo.setTaskName(s1);
        tTaskCompleteVo.setProcessInstanceId(processInstanceId);
        tTaskCompleteVo.setProcessKey(key);
        return tTaskCompleteVo;
    }

    @Override
    public <T> TaskCompleteVo<T> stopProcess(TaskCompleteDto<T> taskCompleteDto) {
        Assert.notNull(taskCompleteDto, "流程操作数据为空，无法执行流程操作，请刷新页面重试！");
        String taskId = taskCompleteDto.getTaskId();
        String comment = taskCompleteDto.getComment();
        T businessData = taskCompleteDto.getBusinessData();
        Assert.notBlank(taskId, "流程任务id为空，无法进行流程操作，请刷新页面重试！");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程已完成或者不存在，无法进行流程操作！");
        String processInstanceId = task.getProcessInstanceId();
        String processDefinitionId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        Assert.notNull(processDefinition, "当前流程定义不存在，请联系管理员解决！");
        String key = processDefinition.getKey();
        comment = StringUtils.isBlank(comment)?TaskCommentConstant.STOPPROCESSMESSAGE:comment;
        // 添加评论，记录流程操作
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKOPERATION, OperationType.STOPPROCESS);
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKMESSAGE, TaskCommentConstant.STOPPROCESSMESSAGE);
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKCOMMENT, comment);
        // 执行流程终止操作
        EndEvent endEventOfProcess = WorkflowUtils.getEndEventOfProcess(processDefinitionId);
        Assert.notNull(endEventOfProcess, "当前流程定义不完整，无法进行流程操作，请联系管理员解决！");
        String id = endEventOfProcess.getId();
        runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId).moveActivityIdTo(taskDefinitionKey, id).changeState();

        // 查询所有流程任务
        Map<String, String> userTaskMapOfProcess = WorkflowUtils.getUserTaskMapOfProcess(processDefinitionId);
        Assert.notEmpty(userTaskMapOfProcess, "当前流程定义错误，请联系管理员处理！");
        String s = userTaskMapOfProcess.get(taskDefinitionKey);
        // 执行生命周期函数
        BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(key);
        T t = null;
        if(ObjectUtils.isNotEmpty(businessWorkflowService)){
            TaskCompleteListenerDto<T> tTaskCompleteListenerDto = new TaskCompleteListenerDto<>();
            tTaskCompleteListenerDto.setProcessKey(key);
            tTaskCompleteListenerDto.setTaskId(taskId);
            tTaskCompleteListenerDto.setTaskName(s);
            tTaskCompleteListenerDto.setBusinessData(businessData);
            tTaskCompleteListenerDto.setProcessInstanceId(processInstanceId);
            t = businessWorkflowService.onProcessStop(tTaskCompleteListenerDto);
        }
        if(ObjectUtils.isEmpty(t)){
            t = businessData;
        }
        // 返回最新数据
        TaskCompleteVo<T> tTaskCompleteVo = new TaskCompleteVo<>();
        tTaskCompleteVo.setProcessInstanceId(processInstanceId);
        tTaskCompleteVo.setTaskId(taskId);
        tTaskCompleteVo.setTaskName(s);
        tTaskCompleteVo.setProcessKey(key);
        tTaskCompleteVo.setBusinessData(t);
        return tTaskCompleteVo;
    }

    @Override
    public <T> TaskCompleteVo<T> completeTask(TaskCompleteDto<T> taskCompleteDto) {
        Assert.notNull(taskCompleteDto, "流程操作数据为空，无法执行流程操作，请刷新页面重试！");
        String taskId = taskCompleteDto.getTaskId();
        T businessData = taskCompleteDto.getBusinessData();
        String comment = taskCompleteDto.getComment();
        comment = StringUtils.isBlank(comment)?TaskCommentConstant.COMPLETETASKMESSAGE:comment;
        Assert.notBlank(taskId, "流程任务id为空，无法执行流程操作！");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if(ObjectUtils.isEmpty(task)){
            throw new RuntimeException("当前流程任务已处理或者当前流程不存在，无法进行流程处理，请刷新页面后重试！");
        }
        String processDefinitionId = task.getProcessDefinitionId();
        String processInstanceId = task.getProcessInstanceId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        Assert.notNull(processDefinition, "当前流程定义不存在，无法进行流程处理，请刷新页面后重试！");
        //　执行流程生命周期数据
        String key = processDefinition.getKey();
        BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(key);
        // 获取所有的任务定义数据
        List<UserTask> userTasks = WorkflowUtils.listAllUserTaskOfProcessDefinition(processDefinitionId);
        Assert.notEmpty(userTasks, "当前流程定义错误，请联系管理员解决！");
        Map<String, String> collect = userTasks.stream().collect(Collectors.toMap(UserTask::getId, UserTask::getName));
        String taskDefinitionKey = task.getTaskDefinitionKey();
        String s1 = collect.get(taskDefinitionKey);
        Assert.notBlank(s1, "当前流程定义存在错误，请联系管理员解决！");
        T t = null;
        if(ObjectUtils.isNotEmpty(businessWorkflowService)){
            TaskCompleteListenerDto<T> tTaskCompleteListenerDto = new TaskCompleteListenerDto<>();
            tTaskCompleteListenerDto.setBusinessData(businessData);
            tTaskCompleteListenerDto.setTaskId(taskId);
            tTaskCompleteListenerDto.setTaskName(s1);
            tTaskCompleteListenerDto.setProcessInstanceId(processInstanceId);
            tTaskCompleteListenerDto.setProcessKey(key);
            t = businessWorkflowService.onTaskComplete(tTaskCompleteListenerDto);
        }
        if(ObjectUtils.isEmpty(t)){
            t = businessData;
        }
        // 添加流程意见，并完成当前任务
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKOPERATION, OperationType.COMPLETETASK);
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKMESSAGE, TaskCommentConstant.COMPLETETASKMESSAGE);
        taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKCOMMENT, comment);
        taskService.complete(taskId);
        // 整理返回数据
        TaskCompleteVo<T> tTaskCompleteVo = new TaskCompleteVo<>();
        tTaskCompleteVo.setBusinessData(t);
        tTaskCompleteVo.setTaskId(taskId);
        tTaskCompleteVo.setTaskName(s1);
        tTaskCompleteVo.setProcessInstanceId(processInstanceId);
        tTaskCompleteVo.setProcessKey(key);
        // 流程顺利通过检测,执行流程结束回调
        FlowElement nextNode = WorkflowUtils.getNextNodeByTaskDefinitionKey(taskDefinitionKey, processDefinitionId);
        if(nextNode instanceof EndEvent){
            TaskCompleteListenerDto<T> tTaskCompleteListenerDto = new TaskCompleteListenerDto<>();
            tTaskCompleteListenerDto.setBusinessData(businessData);
            tTaskCompleteListenerDto.setTaskId(taskId);
            tTaskCompleteListenerDto.setTaskName(s1);
            tTaskCompleteListenerDto.setProcessInstanceId(processInstanceId);
            tTaskCompleteListenerDto.setProcessKey(key);
            businessWorkflowService.onProcessComplete(tTaskCompleteListenerDto);
        }
        return tTaskCompleteVo;
    }

    @Override
    public <T> TaskCompleteVo<T> revokeProcess(String processInstanceId) {
        Assert.notBlank(processInstanceId, "流程实例id为空，无法执行流程操作，请刷新页面重试！");
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
        Assert.notNull(processInstance, "当前流程实例已经被处理或者流程实例不存在，无法进行撤回处理！");

        // 执行声明周期函数
//        BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(key);
        return null;
    }

    @Override
    public <T> TaskCompleteVo<T> recallTask(TaskCompleteDto<T> taskCompleteDto) {
        Assert.notNull(taskCompleteDto, "流程操作数据为空，无法执行流程操作，请刷新页面重试！");
        String taskId = taskCompleteDto.getTaskId();
        String comment = taskCompleteDto.getComment();
        T businessData = taskCompleteDto.getBusinessData();
        // 判断当前流程是否被完成，下一个处理人是否已经处理
        Assert.notBlank(taskId, "当前流程任务为空，无法进行流程处理!");
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        Assert.notNull(historicTaskInstance, "当前流程已处理任务不存在，无法进行流程操作，请刷新页面后重试！");
        String processInstanceId = historicTaskInstance.getProcessInstanceId();
        String taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
        String processDefinitionId = historicTaskInstance.getProcessDefinitionId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
        Assert.notNull(processInstance, "当前流程实例已处理完成或者流程实例不存在，无法进行流程操作！");
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String key = processDefinition.getKey();
        if(list.size()==1){
            Task task = list.get(0);
            String currentTaskDefinitionKey = task.getTaskDefinitionKey();
            FlowElement flowElement = WorkflowUtils.getNextNodeByTaskDefinitionKey(taskDefinitionKey, processDefinitionId);
            String id = flowElement.getId();
            // 判断是否可以进行拿回操作
            if(StringUtils.equals(id, currentTaskDefinitionKey)){
                // 添加流程comment
                comment = StringUtils.isBlank(comment)?TaskCommentConstant.RECALLTASKMESSAGE:comment;
                taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKOPERATION, OperationType.RECALLTASK);
                taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKMESSAGE, TaskCommentConstant.RECALLTASKMESSAGE);
                taskService.addComment(taskId, processInstanceId, TaskCommentConstant.TASKCOMMENT, comment);
                runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId).moveActivityIdTo(currentTaskDefinitionKey, taskDefinitionKey).changeState();
                // 执行声明周期函数
                Map<String, String> userTaskMapOfProcess = WorkflowUtils.getUserTaskMapOfProcess(processDefinitionId);
                String s = userTaskMapOfProcess.get(taskId);
                BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(key);
                T t = null;
                if(ObjectUtils.isNotEmpty(businessWorkflowService)){
                    TaskCompleteListenerDto<T> tTaskCompleteListenerDto = new TaskCompleteListenerDto<>();
                    tTaskCompleteListenerDto.setTaskId(taskId);
                    tTaskCompleteListenerDto.setTaskName(s);
                    tTaskCompleteListenerDto.setProcessKey(key);
                    tTaskCompleteListenerDto.setBusinessData(businessData);
                    tTaskCompleteListenerDto.setProcessInstanceId(processInstanceId);
                    t = businessWorkflowService.onTaskRecall(tTaskCompleteListenerDto);
                }
                if(ObjectUtils.isEmpty(t)){
                    t = businessData;
                }
                TaskCompleteVo<T> tTaskCompleteVo = new TaskCompleteVo<>();
                tTaskCompleteVo.setBusinessData(t);
                tTaskCompleteVo.setTaskId(taskId);
                tTaskCompleteVo.setTaskName(s);
                tTaskCompleteVo.setProcessKey(key);
                tTaskCompleteVo.setProcessInstanceId(processInstanceId);
                return tTaskCompleteVo;
            }else{
                throw new RuntimeException("当前流程后续节点已经被处理，无法进行流程审批拿回操作！");
            }
        }else{
            throw new RuntimeException("当前流程后续节点已经被处理，无法进行流程审批拿回操作！");
        }
    }

    @Override
    public <T> TaskCompleteVo<T> assigneeTask(TaskAssigneeDto<T> taskAssigneeDto) {
        Assert.notNull(taskAssigneeDto, "流程操作数据为空，无法执行流程操作，请刷新页面重试！");
        String assignee = taskAssigneeDto.getAssignee();
        String taskId = taskAssigneeDto.getTaskId();
        String comment = taskAssigneeDto.getComment();
        T businessData = taskAssigneeDto.getBusinessData();
        if(StringUtils.isAnyBlank(taskId, assignee)){
            throw new RuntimeException("当前流程任务id或者转办人id为空，无法进行流程转办操作，请刷新页面重试！");
        }
        comment = StringUtils.isBlank(comment)?TaskCommentConstant.ASSIGNEETASKMESSAGE:comment;
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在，无法进行流程处理！");
        String assignee1 = task.getAssignee();
        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        Assert.notNull(processDefinition, "当前流程定义不存在，无法进行流程操作，请联系管理员结局！");
        String key = processDefinition.getKey();
        String processInstanceId = task.getProcessInstanceId();
        // 保留流程转办记录
        taskService.addComment(taskId, processInstanceId, OperationType.ASSIGNEETASK,assignee1 + "-->" + assignee + ":" + comment);
        // 执行转办
        taskService.setAssignee(taskId, assignee);
        Map<String, String> map = WorkflowUtils.getUserTaskMapOfProcess(processDefinitionId);
        Assert.notEmpty(map, "当前流程定义错误，请联系管理员解决！");
        String s = map.get(taskId);
        BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(key);
        T t = null;
        if(ObjectUtils.isNotEmpty(businessWorkflowService)){
            TaskAssigneeListenerDto<T> taskAssigneeListenerDto = new TaskAssigneeListenerDto<>();
            taskAssigneeListenerDto.setNewAssignee(assignee);
            taskAssigneeListenerDto.setOldAssignee(assignee1);
            taskAssigneeListenerDto.setComment(comment);
            taskAssigneeListenerDto.setBusinessData(businessData);
            taskAssigneeListenerDto.setTaskId(taskId);
            taskAssigneeListenerDto.setTaskName(s);
            t = businessWorkflowService.onTaskAssignee(taskAssigneeListenerDto);
        }
        if(ObjectUtils.isEmpty(t)){
            t = businessData;
        }
        TaskCompleteVo<T> tTaskCompleteVo = new TaskCompleteVo<>();
        tTaskCompleteVo.setTaskId(taskId);
        tTaskCompleteVo.setTaskName(s);
        tTaskCompleteVo.setBusinessData(t);
        tTaskCompleteVo.setProcessKey(key);
        tTaskCompleteVo.setProcessInstanceId(processInstanceId);
        return tTaskCompleteVo;
    }
}
