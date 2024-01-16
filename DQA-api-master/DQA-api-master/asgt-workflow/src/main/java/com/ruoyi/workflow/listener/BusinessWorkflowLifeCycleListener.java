package com.ruoyi.workflow.listener;

import com.ruoyi.workflow.model.dto.*;

/**
 * Created by Axel on 2023/6/7 16:49
 * 业务流程生命周期监听服务
 * @author Axel
 */
public interface BusinessWorkflowLifeCycleListener {
    /**
     * 业务流程启动事件
     * @param processStartListenerDto  流程启动业务回填数据
     * @return 进行更改之后的业务数据
     */
    <T> T onProcessStart(ProcessStartListenerDto<T> processStartListenerDto);

    /**
     * 业务流程顺利完成事件
     * @param <T> 业务数据类型
     * @param taskCompleteDto 任务完成回填数据
     * @return 业务数据
     */
    <T> T onProcessComplete(TaskCompleteListenerDto<T> taskCompleteDto);

    /**
     * 业务流程驳回事件
     * @param taskRejectListenerDto 流程驳回回填数据
     * @param <T> 业务数据
     * @return 业务数据
     */
    <T> T onProcessReject(TaskRejectListenerDto<T> taskRejectListenerDto);

    /**
     * 业务流程被拒绝事件
     * @param taskCompleteDto 任务拒绝数据
     * @param <T> 业务数据
     * @return 业务数据
     */
    <T> T onProcessStop(TaskCompleteListenerDto<T> taskCompleteDto);

    /**
     * 流程任务被通过
     * @param taskCompleteListenerDto 流程通过时回填数据
     * @return 流程完成时回传的业务数据
     */
    <T> T onTaskComplete(TaskCompleteListenerDto<T> taskCompleteListenerDto);

    /**
     * 流程转办
     * @param taskAssigneeListenerDto 流程转办数据
     * @param <T> 业务数据
     * @return 业务数据
     */
    <T> T onTaskAssignee(TaskAssigneeListenerDto<T> taskAssigneeListenerDto);

    /**
     * 流程拿回监听事件
     * @param taskCompleteListenerDto 流程任务回填数据
     * @param <T> 业务数据类型
     * @return 流程业务数据返回
     */
    <T> T onTaskRecall(TaskCompleteListenerDto<T> taskCompleteListenerDto);

    <T> T onProcessRevoke();
}
