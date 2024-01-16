package com.ruoyi.workflow.constant;

/**
 * Created by Axel on 2023/6/7 19:08
 *
 * @author Axel
 */
public class OperationType {
    /**
     * 流程启动
     */
    public static final String STARTPROCESS = "processStart";
    /**
     * 流程完成
     */
    public static final String COMPLETEPROCESS = "processComplete";
    /**
     * 流程拒绝
     */
    public static final String STOPPROCESS = "processStop";
    /**
     * 流程撤回
     */
    public static final String REVOKEPROCESS = "processRevoke";

    /**
     * 任务完成
     */
    public static final String COMPLETETASK = "taskComplete";
    /**
     * 流程驳回，支持驳回到历史任一节点
     */
    public static final String REJECTTASK = "taskReject";
    /**
     * 审批拿回
     */
    public static final String RECALLTASK = "taskRecall";
    /**
     * 流程转办
     */
    public static final String ASSIGNEETASK = "taskAssginee";
}
