package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/12 15:52
 *
 * @author Axel
 */
@Data
public class TaskCompleteJsonDto {
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 业务数据
     */
    private String businessData;
    /**
     * 审批意见
     */
    private String comment;
}
