package com.ruoyi.srm.supplierBase.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.srm.supplierBase.domain.DTO.PotentialRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.VO.PotentialReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.PotentialRequirementEntity;

import java.util.List;

/**
 * @author: parus
 * @date: 2023/6/5 16:14
 */
public interface PotentialRequirementService extends IService<PotentialRequirementEntity> {

    IPage<PotentialRequirementEntity> getPotentialReqByPage(Integer pageNum,Integer pageSize);

    /**
     * 根据流程实例id获取信息
     * @param processInstanceId 流程实例id
     * @return 潜在需求
     */
    PotentialReqVO getPotentialByProcessInstanceId(String processInstanceId);

    void addPotentialReq(PotentialRequirementDTO dto);

    void updatePotentialReq(PotentialRequirementDTO dto);

    PotentialRequirementDTO getPotentialById(String id);
}
