package com.ruoyi.srm.supplierBase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchChangeLogDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ChangeRecordsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ChangeRecordsEntity;

import java.util.List;

/**
 * @author: dyw
 * @date: 2023/5/25 15:43
 */
public interface ChangeLogService extends IService<ChangeRecordsEntity> {
    /**
     * 添加变更记录
     * @param entity 变更的供应商实体
     */
    void addChangeLog(ChangeRecordsEntity entity);

    /**
     * 获取变更记录详情
     * @param id 变更记录id
     * @return 变更记录vo
     */
    ChangeRecordsVO getChangeLog(String id);

    /**
     * 根据流程实例id
     * @param processInstanceId 流程实例id
     * @return 变更记录
     */
    ChangeRecordsEntity getChangeByProcessInstanceId(String processInstanceId);

    /**
     * 获取分页的变更记录
     * @return 返回分页数据
     */
    List<ChangeRecordsVO> getChangeLogByPage(SearchChangeLogDTO dto);
}
