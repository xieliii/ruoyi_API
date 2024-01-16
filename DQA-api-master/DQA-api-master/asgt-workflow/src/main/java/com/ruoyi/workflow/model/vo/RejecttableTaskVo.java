package com.ruoyi.workflow.model.vo;

import lombok.Data;

/**
 * Created by Axel on 2023/6/19 17:14
 *
 * @author Axel
 */
@Data
public class RejecttableTaskVo {
    private String processInstanceId;
    private String taskName;
    private String taskKey;
}
