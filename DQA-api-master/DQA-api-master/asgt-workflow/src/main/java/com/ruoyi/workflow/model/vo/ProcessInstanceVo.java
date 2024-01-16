package com.ruoyi.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by Axel on 2023/6/9 16:46
 *
 * @author Axel
 */
@Data
public class ProcessInstanceVo {
    private String processDefinitionId;
    private String processName;
    private String processInstanceId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date finishTime;
    private String startUser;
    private String startUserId;
    private String processStatus;
    private String currentNode;
    private String currentNodeKey;
    private String duration;
    private String currentAssigneeId;
    private String currentAssignee;
    private String processKey;
}
