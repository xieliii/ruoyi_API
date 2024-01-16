package com.ruoyi.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.workflow.model.vo.ProcessInstanceDetailVo;
import com.ruoyi.workflow.model.vo.ProcessInstanceVo;
import com.ruoyi.workflow.model.vo.ProcessStatisticVo;
import com.ruoyi.workflow.model.vo.RejecttableTaskVo;

import java.util.List;

/**
 * Created by Axel on 2023/6/9 16:44
 *
 * @author Axel
 */
public interface WorkflowProcessInstanceService {
    /**
     * 分页查询流程实例信息
     * @param current 当前分页数据
     * @param pageSize 分页大小
     * @return 流程实例分页数据
     */
    Page<ProcessInstanceVo> listPageProcessInstance(Integer current, Integer pageSize);

    /**
     * 根据查询条件查询流程实例
     * @param current 当前分页数据
     * @param pageSize 分页大小
     * @param name 名称
     * @return 流程实例分页数据
     */
    Page<ProcessInstanceVo> listPageProcessInstanceByQueryCondition(Integer current, Integer pageSize, String name);
    /**
     * 根据流程实例id获取流程实例详情
     * @param processInstanceId 流程实例id
     * @return 流程实例详情信息
     */
    ProcessInstanceDetailVo getProcessInstanceDetailById(String processInstanceId);

    /**
     * 根据流程实例获取所有可驳回信息
     * @param processInstanceId 流程实例id
     * @return 流程节点信息
     */
    List<RejecttableTaskVo> listRejecttableTaskOfProcess(String processInstanceId);

    /**
     * 获取流程统计信息
     * @return 流程统计数据
     */
    List<ProcessStatisticVo> listProcessStatistic();
}
