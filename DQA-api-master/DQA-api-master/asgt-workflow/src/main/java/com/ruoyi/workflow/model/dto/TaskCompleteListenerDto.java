package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/8 18:40
 *
 * @author Axel
 */
@Data
public class TaskCompleteListenerDto<T> {
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

    private String processKey;
}
