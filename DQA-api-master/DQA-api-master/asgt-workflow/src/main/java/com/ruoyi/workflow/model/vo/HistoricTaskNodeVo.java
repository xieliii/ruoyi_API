package com.ruoyi.workflow.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Axel on 2023/6/8 10:52
 *
 * @author Axel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricTaskNodeVo {
    private String processDefinitionId;
    private String taskDefinitionKey;
    private String name;
}
