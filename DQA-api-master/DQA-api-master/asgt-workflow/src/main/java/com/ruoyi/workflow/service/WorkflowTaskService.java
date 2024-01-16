package com.ruoyi.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.workflow.model.vo.DoneTaskVo;
import com.ruoyi.workflow.model.vo.TaskVo;

/**
 * Created by Axel on 2023/6/9 8:53
 *
 * @author Axel
 */
public interface WorkflowTaskService{
    /**
     * 分页查询所有的流程任务
     * @param current 分页参数
     * @param pageSize 分页参数
     * @return 任务数据
     */
    Page<TaskVo> listPageTask(Integer current, Integer pageSize);

    /**
     * 分页查询所有流程任务
     * @param current 分页参数
     * @param pageSize 分页参数
     * @param name 流程名称
     * @return 任务数据
     */
    Page<TaskVo> listPageTaskByQueryCondition(Integer current, Integer pageSize, String name);

    /**
     * 分页查询所有的已办流程任务
     * @param current 分页参数
     * @param pageSize 分页参数
     * @return 已办流程任务
     */
    Page<DoneTaskVo> listPageFinishedTask(Integer current, Integer pageSize);

    /**
     * 分页查询所有已办流程任务
     * @param current 分页参数
     * @param pageSize 分页参数
     * @param name 流程名称
     * @return 任务数据
     */
    Page<DoneTaskVo> listPageFinishedTaskByQueryCondition(Integer current, Integer pageSize, String name);
}
