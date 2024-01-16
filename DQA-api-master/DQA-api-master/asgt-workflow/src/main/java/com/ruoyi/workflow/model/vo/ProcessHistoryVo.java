package com.ruoyi.workflow.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by Axel on 2023/6/15 8:58
 *
 * @author Axel
 */
@Data
public class ProcessHistoryVo {
    private String taskId;
    private String operationType;
    private String assignee;
    private String assigneeName;
    private String duration;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date receiveTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date completeTime;
    private String comment;
    private String nodeName;
}
