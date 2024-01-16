package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/12 15:06
 *
 * @author Axel
 */
@Data
public class ProcessAssigneeDto{
    private String processKey;
    private String assignee;
    private String taskNodeKey;
    private String processInstanceId;
}
