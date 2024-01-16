package com.ruoyi.workflow.constant;

/**
 * Created by Axel on 2023/6/7 20:16
 *
 * @author Axel
 */
public class TaskCommentConstant {
    /**
     * 操作类型
     */
    public static final String TASKOPERATION = "taskOperation";
    /**
     * 审批信息
     */
    public static final String TASKMESSAGE = "taskMessage";
    /**
     * 审批意见
     */
    public static final String TASKCOMMENT = "comment";



    /**
     * 流程启动
     */
    public static final String STARTPROCESSMESSAGE = "启动";
    /**
     * 流程完成
     */
    public static final String COMPLETEPROCESSMESSAGE = "完成";
    /**
     * 流程拒绝
     */
    public static final String STOPPROCESSMESSAGE = "拒绝";
    /**
     * 流程撤回
     */
    public static final String REVOKEPROCESSMESSAGE = "撤回";

    /**
     * 任务完成
     */
    public static final String COMPLETETASKMESSAGE = "通过";
    /**
     * 流程驳回，支持驳回到历史任一节点
     */
    public static final String REJECTTASKMESSAGE = "驳回";
    /**
     * 审批拿回
     */
    public static final String RECALLTASKMESSAGE = "拿回";
    /**
     * 流程转办
     */
    public static final String ASSIGNEETASKMESSAGE = "转办";
}
