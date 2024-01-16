package com.ruoyi.workflow.service;

import com.ruoyi.workflow.model.dto.*;
import com.ruoyi.workflow.model.vo.ProcessStartVo;
import com.ruoyi.workflow.model.vo.TaskCompleteVo;

/**
 * Created by Axel on 2023/6/12 15:49
 *
 * @author Axel
 */
public interface WorkflowIntegrateService {
    /**
     * 启动流程
     * @param processStartDto 流程启动数据
     * @return 流程启动成功流程数据
     */
    ProcessStartVo startProcess(ProcessStartJsonDto processStartDto);

    /**
     * 流程驳回
     * @param taskRejectDto 流程任务数据
     * @return 流程驳回结果
     */
    TaskCompleteVo rejectProcess(TaskRejectJsonDto taskRejectDto);

    /**
     * 流程拒绝
     * @param taskCompleteDto 流程任务数据
     * @return 流程拒绝结果
     */
    TaskCompleteVo stopProcess(TaskCompleteJsonDto taskCompleteDto);

    /**
     * 通过流程
     * @param taskCompleteDto 流程任务数据
     * @return 流程通过结果
     */
    TaskCompleteVo completeTask(TaskCompleteJsonDto taskCompleteDto);


    /**
     * 流程撤回
     * @param processInstanceId 流程实例id
     * @return 撤回结果
     */
    TaskCompleteVo revokeProcess(String processInstanceId);

    /**
     * 在下一个节点未被处理时，可以拿回审批重新审批
     * @param taskCompleteDto 拿回审批
     * @return 拿回结果
     */
    TaskCompleteVo recallTask(TaskCompleteJsonDto taskCompleteDto);

    /**
     * 流程转办
     * @param taskAssigneeDto 流程转办数据
     * @return 转办结果
     */
    TaskCompleteVo assigneeTask(TaskAssigneeJsonDto taskAssigneeDto);
}
