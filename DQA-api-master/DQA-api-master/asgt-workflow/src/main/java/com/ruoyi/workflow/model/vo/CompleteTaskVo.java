package com.ruoyi.workflow.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by Axel on 2023/6/9 16:48
 *
 * @author Axel
 */
@Data
public class CompleteTaskVo<T> {
    private String taskId;
    private String taskName;
    private String startUserId;
    private String startUser;
    private Date receiveTime;
    private Date completeTime;
    private Date duration;
    private String processInstanceId;
    /**
     * 业务数据
     */
    private T businessData;
}
