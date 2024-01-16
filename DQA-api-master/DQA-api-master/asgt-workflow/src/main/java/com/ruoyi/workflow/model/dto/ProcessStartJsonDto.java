package com.ruoyi.workflow.model.dto;

import com.ruoyi.workflow.annotaions.ProcessKey;
import lombok.Data;

/**
 * Created by Axel on 2023/6/12 15:47
 *
 * @author Axel
 */
@Data
@ProcessKey("srm-process")
public class ProcessStartJsonDto {
    private String businessData;
    private String processKey;
}
