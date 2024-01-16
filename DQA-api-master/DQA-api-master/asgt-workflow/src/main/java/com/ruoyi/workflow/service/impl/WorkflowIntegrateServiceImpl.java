package com.ruoyi.workflow.service.impl;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.workflow.adapter.AbstractBusinessOperationAdapter;
import com.ruoyi.workflow.config.BusinessWorkflowConfig;
import com.ruoyi.workflow.factory.FlowServiceFactory;
import com.ruoyi.workflow.model.dto.*;
import com.ruoyi.workflow.model.vo.ProcessStartVo;
import com.ruoyi.workflow.model.vo.TaskCompleteVo;
import com.ruoyi.workflow.service.WorkflowAssigneeService;
import com.ruoyi.workflow.service.WorkflowIntegrateService;
import com.ruoyi.workflow.service.WorkflowOperationService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by Axel on 2023/6/12 15:49
 *
 * @author Axel
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowIntegrateServiceImpl extends FlowServiceFactory implements WorkflowIntegrateService {
    @Resource
    private WorkflowOperationService workflowOperationService;
    @Resource
    private WorkflowAssigneeService assigneeService;

    @Override
    public ProcessStartVo startProcess(ProcessStartJsonDto processStartDto) {
        Assert.notNull(processStartDto, "当前流程启动数据为空，无法进行流程发起！");
        String businessData = processStartDto.getBusinessData();
        String processKey = processStartDto.getProcessKey();
        if(StringUtils.isAnyBlank(businessData, processKey)){
            throw new RuntimeException("当前流程业务数据或者流程键为空，无法正确发起流程，请刷新页面后重试！");
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).latestVersion().singleResult();
        Assert.notNull(processDefinition, "当前流程键不存在对应的流程，无法启动流程，请联系管理员解决！");
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(processKey);
        Object entity = businessOperationAdapter.createEntity(businessData);
        // 发起流程
        ProcessStartDto<Object> objectProcessStartDto = new ProcessStartDto<>();
        objectProcessStartDto.setBusinessData(entity);
        objectProcessStartDto.setBusinessKey(processKey);
        ProcessStartVo<Object> objectProcessStartVo = workflowOperationService.startProcess(objectProcessStartDto);
        String processInstanceId = objectProcessStartVo.getProcessInstanceId();
        // 进行数据转换并保存业务数据
        ProcessStartAdapterDto<Object> objectProcessStartAdapterDto = new ProcessStartAdapterDto<>();
        objectProcessStartAdapterDto.setProcessKey(processKey);
        objectProcessStartAdapterDto.setProcessInstanceId(processInstanceId);
        objectProcessStartAdapterDto.setBusinessData(entity);
        businessOperationAdapter.saveBusinessData(objectProcessStartAdapterDto);
        return objectProcessStartVo;
    }

    @Override
    public TaskCompleteVo rejectProcess(TaskRejectJsonDto taskRejectDto) {
        Assert.notNull(taskRejectDto, "流程操作数据为空，无法进行流程操作！");
        String taskId = taskRejectDto.getTaskId();
        String rejectTaskKey = taskRejectDto.getRejectTaskKey();
        String comment = taskRejectDto.getComment();
        String businessData = taskRejectDto.getBusinessData();
        if(StringUtils.isAnyBlank(taskId, rejectTaskKey)){
            throw new RuntimeException("当前流程任务id或者驳回节点为空，无法进行流程操作，请刷新页面后重试！");
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务已被处理或者不存在，请刷新页面后重试！");
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String key = processDefinition.getKey();
        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(key);
        // 进行数据转换并更新业务数据
        Object entity = businessOperationAdapter.createEntity(businessData);
        businessOperationAdapter.updateBusinessData(entity);
        TaskRejectDto<Object> objectTaskRejectDto = new TaskRejectDto<>();
        objectTaskRejectDto.setRejectTaskKey(rejectTaskKey);
        objectTaskRejectDto.setBusinessData(entity);
        objectTaskRejectDto.setComment(comment);
        objectTaskRejectDto.setTaskId(taskId);
        return workflowOperationService.rejectProcess(objectTaskRejectDto);
    }

    @Override
    public TaskCompleteVo stopProcess(TaskCompleteJsonDto taskCompleteDto) {
        Assert.notNull(taskCompleteDto, "流程操作数据为空，无法进行流程操作！");
        String taskId = taskCompleteDto.getTaskId();
        String comment = taskCompleteDto.getComment();
        String businessData = taskCompleteDto.getBusinessData();
        Assert.notBlank(taskId, "当前流程任务id为空，无法进行流程操作！");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务已被处理或者不存在，请刷新页面后重试！");
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String key = processDefinition.getKey();
        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(key);
        Object entity = businessOperationAdapter.createEntity(businessData);
        businessOperationAdapter.updateBusinessData(entity);
        TaskCompleteDto<Object> objectTaskCompleteDto = new TaskCompleteDto<>();
        objectTaskCompleteDto.setTaskId(taskId);
        objectTaskCompleteDto.setComment(comment);
        objectTaskCompleteDto.setBusinessData(businessData);
        return workflowOperationService.stopProcess(objectTaskCompleteDto);
    }

    @Override
    public TaskCompleteVo completeTask(TaskCompleteJsonDto taskCompleteDto) {
        Assert.notNull(taskCompleteDto, "当前流程数据为空，无法进行流程操作！");
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
        String taskId = taskCompleteDto.getTaskId();
        Assert.notEmpty(taskId, "当前流程任务id为空，无法进行流程操作！");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在或者已被处理，请刷新页面后重试！");
        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String key = processDefinition.getKey();
        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(key);
        Object entity = businessOperationAdapter.createEntity(taskCompleteDto.getBusinessData());
        businessOperationAdapter.updateBusinessData(entity);
        TaskCompleteDto<Object> objectTaskCompleteDto = new TaskCompleteDto<>();
        objectTaskCompleteDto.setTaskId(taskId);
        objectTaskCompleteDto.setBusinessData(entity);
        objectTaskCompleteDto.setComment(taskCompleteDto.getComment());
        return workflowOperationService.completeTask(objectTaskCompleteDto);
    }

    @Override
    public TaskCompleteVo revokeProcess(String processInstanceId) {
        return null;
    }

    @Override
    public TaskCompleteVo recallTask(TaskCompleteJsonDto taskCompleteDto) {
        Assert.notNull(taskCompleteDto, "当前流程数据为空，无法进行流程操作！");
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
        String taskId = taskCompleteDto.getTaskId();
        Assert.notEmpty(taskId, "当前流程任务id为空，无法进行流程操作！");
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在或者已被处理，请刷新页面后重试！");
        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String key = processDefinition.getKey();
        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(key);
        Object entity = businessOperationAdapter.createEntity(taskCompleteDto.getBusinessData());
        businessOperationAdapter.updateBusinessData(entity);
        TaskCompleteDto<Object> objectTaskCompleteDto = new TaskCompleteDto<>();
        objectTaskCompleteDto.setTaskId(taskId);
        objectTaskCompleteDto.setBusinessData(entity);
        objectTaskCompleteDto.setComment(taskCompleteDto.getComment());
        return workflowOperationService.recallTask(objectTaskCompleteDto);
    }

    @Override
    public TaskCompleteVo assigneeTask(TaskAssigneeJsonDto taskAssigneeDto) {
        Assert.notNull(taskAssigneeDto, "流程操作数据为空，无法进行流程操作！");
        String taskId = taskAssigneeDto.getTaskId();
        String comment = taskAssigneeDto.getComment();
        String businessData = taskAssigneeDto.getBusinessData();
        String assignee = taskAssigneeDto.getAssignee();
        if(StringUtils.isAnyBlank(assignee, taskId, businessData)){
            throw new RuntimeException("当前流程任务参数缺失，无法进行流程操作！");
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在或者已被处理，请刷新页面后重试！");
        String processDefinitionId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String key = processDefinition.getKey();
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(key);
        Object entity = businessOperationAdapter.createEntity(businessData);
        businessOperationAdapter.updateBusinessData(entity);
        TaskAssigneeDto<Object> dto = new TaskAssigneeDto<>();
        dto.setAssignee(assignee);
        dto.setTaskId(taskId);
        dto.setComment(comment);
        dto.setBusinessData(entity);
        return workflowOperationService.assigneeTask(dto);
    }
}
