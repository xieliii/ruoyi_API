package com.ruoyi.srm.supplierBase.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.srm.supplierBase.domain.DTO.ApprovedRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.VO.WorkflowRsApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.ApprovedRequirementEntity;

/**
 * @author: parus
 * @date: 2023/6/5 16:13
 */
public interface ApprovedRequirementService extends IService<ApprovedRequirementEntity> {

    IPage<ApprovedRequirementEntity> getApprovedReqByPage(Integer pageNum,Integer pageSize);

    /**
     * 根据流程实例id获取信息
     * @param processInstanceId 流程实例id
     * @return 获批需求
     */
    WorkflowRsApprovedReqVO getApprovedByProcessInstanceId(String processInstanceId);

    /**
     * 同上 区分rfi rfp rfq
     * @param id 流程实例id
     * @param processKey 流程key
     * @return 获批需求
     */
    WorkflowRsApprovedReqVO getApprovedReqByProcess(String id,String processKey);

    void addApprovedReq(ApprovedRequirementDTO dto);

    void updateApprovedReq(ApprovedRequirementDTO dto);

    ApprovedRequirementDTO getApprovedById(String id);
}
