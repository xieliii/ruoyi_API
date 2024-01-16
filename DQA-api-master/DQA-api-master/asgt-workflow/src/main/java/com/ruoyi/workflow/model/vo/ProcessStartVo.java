package com.ruoyi.workflow.model.vo;

import lombok.Data;

/**
 * Created by Axel on 2023/6/7 18:19
 *
 * @author Axel
 */
@Data
public class ProcessStartVo<T> {
    private String processKey;
    private String processInstanceId;
    private T businessData;
}
