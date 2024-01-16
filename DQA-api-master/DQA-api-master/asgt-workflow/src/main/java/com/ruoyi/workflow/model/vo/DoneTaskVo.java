package com.ruoyi.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by Axel on 2023/6/12 10:31
 *
 * @author Axel
 */
@Data
public class DoneTaskVo {
    private String taskId;
    private String processName;
    private String processDefinitionId;
    private String taskName;
    private String startUserId;
    private String startUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date receiveTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date processStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date finishTime;
    private String processInstanceId;
    private String duration;
    private String processKey;
    private String processOperationType;
}
