package com.ruoyi.srm.supplierBase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.srm.supplierBase.domain.entity.PotentialRequirementEntity;

/**
 * @author: dyw
 * @date: 2023/5/25 16:22
 */
public interface PotentialRequirementMapper extends BaseMapper<PotentialRequirementEntity> {

    /**
     * 根据流程实例id获取潜在入册需求实体
     * @param processInstanceId 流程实例id
     * @return 潜在入册需求实体
     */
    PotentialRequirementEntity getPotentialByProcessInstanceId(String processInstanceId);
}
