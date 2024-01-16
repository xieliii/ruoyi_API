package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/7 17:37
 *
 * @author Axel
 */
@Data
public class ProcessExecutionDto<T> {
    /**
     * 流程键
     */
    private String processKey;
    /**
     * 业务数据
     */
    private T t;


    private String eventName;
}
