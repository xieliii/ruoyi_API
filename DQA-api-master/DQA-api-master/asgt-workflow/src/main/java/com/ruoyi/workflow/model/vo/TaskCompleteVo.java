package com.ruoyi.workflow.model.vo;

import lombok.Data;

/**
 * Created by Axel on 2023/6/7 18:20
 *
 * @author Axel
 */
@Data
public class TaskCompleteVo<T> {
    private T businessData;
    private String processInstanceId;
    private String taskId;
    private String taskName;
    private String processKey;
}
