package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/7 18:20
 *
 * @author Axel
 */
@Data
public class TaskCompleteDto<T> {
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
}
