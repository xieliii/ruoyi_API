package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/14 15:59
 *
 * @author Axel
 */
@Data
public class ProcessStartAdapterDto<T> {
    private String processInstanceId;
    private T businessData;
    private String processKey;
}

