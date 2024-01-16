package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/8 19:23
 *
 * @author Axel
 */
@Data
public class TaskRejectDto<T> {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 业务数据
     */
    private T businessData;
    /**
     * 审批意见
     */
    private String comment;
    /**
     * 驳回节点
     */
    private String rejectTaskKey;
}
