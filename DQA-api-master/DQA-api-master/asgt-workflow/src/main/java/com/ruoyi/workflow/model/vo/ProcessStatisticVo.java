package com.ruoyi.workflow.model.vo;

import lombok.Data;

/**
 * Created by Axel on 2023/6/26 11:51
 *
 * @author Axel
 */
@Data
public class ProcessStatisticVo {
    private String processName;
    private String processKey;
    private Long runningCount;
    private Long finishedCount;
}
