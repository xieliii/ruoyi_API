package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/8 19:24
 *
 * @author Axel
 */
@Data
public class TaskAssigneeDto<T> {
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
     * 转办人员
     */
    private String assignee;
}
