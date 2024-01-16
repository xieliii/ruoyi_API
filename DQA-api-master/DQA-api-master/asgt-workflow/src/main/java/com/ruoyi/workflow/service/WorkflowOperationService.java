package com.ruoyi.workflow.service;

import com.ruoyi.workflow.model.dto.ProcessStartDto;
import com.ruoyi.workflow.model.dto.TaskAssigneeDto;
import com.ruoyi.workflow.model.dto.TaskCompleteDto;
import com.ruoyi.workflow.model.dto.TaskRejectDto;
import com.ruoyi.workflow.model.vo.ProcessStartVo;
import com.ruoyi.workflow.model.vo.TaskCompleteVo;

/**
 * Created by Axel on 2023/6/7 11:53
 *
 * @author Axel
 */
public interface WorkflowOperationService {
    /**
     * 启动流程
     * @param processStartDto 流程启动数据
     * @return 流程启动成功流程数据
     */
    <T> ProcessStartVo<T> startProcess(ProcessStartDto<T> processStartDto);

    /**
     * 流程驳回
     * @param taskRejectDto 流程任务数据
     * @return 流程驳回结果
     */
    <T> TaskCompleteVo<T> rejectProcess(TaskRejectDto<T> taskRejectDto);

    /**
     * 流程拒绝
     * @param taskCompleteDto 流程任务数据
     * @return 流程拒绝结果
     */
    <T> TaskCompleteVo<T> stopProcess(TaskCompleteDto<T> taskCompleteDto);

    /**
     * 通过流程
     * @param taskCompleteDto 流程任务数据
     * @return 流程通过结果
     */
    <T> TaskCompleteVo<T> completeTask(TaskCompleteDto<T> taskCompleteDto);


    /**
     * 流程撤回
     * @param processInstanceId 流程实例id
     * @return 撤回结果
     */
    <T> TaskCompleteVo<T> revokeProcess(String processInstanceId);

    /**
     * 在下一个节点未被处理时，可以拿回审批重新审批
     * @param taskCompleteDto 拿回审批
     * @return 拿回结果
     */
    <T> TaskCompleteVo<T> recallTask(TaskCompleteDto<T> taskCompleteDto);

    /**
     * 流程转办
     * @param taskAssigneeDto 流程转办数据
     * @return 转办结果
     */
    <T> TaskCompleteVo<T> assigneeTask(TaskAssigneeDto<T> taskAssigneeDto);


}
