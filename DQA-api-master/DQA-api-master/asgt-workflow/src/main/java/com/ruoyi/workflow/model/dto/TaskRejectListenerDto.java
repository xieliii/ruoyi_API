package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/8 19:54
 *
 * @author Axel
 */
@Data
public class TaskRejectListenerDto<T> {
    /**
     * 业务数据
     */
    private T businessData;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 待驳回节点key
     */
    private String rejectTaskKey;
    /**
     * 待驳回节点name
     */
    private String rejectTaskName;

    private String processKey;
}
