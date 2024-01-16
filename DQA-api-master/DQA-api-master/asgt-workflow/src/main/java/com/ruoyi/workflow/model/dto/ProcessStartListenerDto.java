package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/7 19:31
 *
 * @author Axel
 */
@Data
public class ProcessStartListenerDto<T> {
    private String processInstanceId;
    private T businessData;
}
