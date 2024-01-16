package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/9 10:04
 *
 * @author Axel
 */
@Data
public class TaskAssigneeListenerDto<T> {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
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
    private String newAssignee;
    /**
     * 旧处理人
     */
    private String oldAssignee;
}
