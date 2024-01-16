package com.ruoyi.workflow.model.vo;

import lombok.Data;

import java.util.List;

/**
 * Created by Axel on 2023/6/15 8:56
 *
 * @author Axel
 */
@Data
public class ProcessInstanceDetailVo {
    private String processInstanceId;
    private String processDefinitionId;
    private String xmlData;
    private List<ProcessHistoryVo> historyList;
    private List<ProcessNodeVo> nodes;
    private String processStatus;
    private String activeTaskId;
    private String processKey;
    private String activeTaskKey;
    private Boolean isStartUserTask;
}
