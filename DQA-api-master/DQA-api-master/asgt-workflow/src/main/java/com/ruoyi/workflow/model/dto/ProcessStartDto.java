package com.ruoyi.workflow.model.dto;

import lombok.Data;

/**
 * Created by Axel on 2023/6/7 11:39
 *
 * @author Axel
 */
@Data
public class ProcessStartDto<T> {
    /**
     * 流程键，根据流程键启动流程，
     * 保证流程启动时选择的是最新版的流程
     */
    private String businessKey;

    /**
     * 业务数据，必须包含id属性
     * id唯一标识业务数据
     */
    private T businessData;


}
