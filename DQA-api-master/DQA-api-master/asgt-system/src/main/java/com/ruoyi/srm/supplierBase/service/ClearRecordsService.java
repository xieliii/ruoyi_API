package com.ruoyi.srm.supplierBase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.srm.supplierBase.domain.DTO.ClearRecordsDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchClearRecordsDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ClearRecordsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ClearRecordsEntity;

import java.util.List;

/**
 * @author: dyw
 * @date: 2023/6/1 13:52
 */
public interface ClearRecordsService extends IService<ClearRecordsEntity> {

    List<ClearRecordsVO> getClearList(SearchClearRecordsDTO dto);

    /**
     * 根据流程实例id获取详情
     * @param instanceId 流程实例id
     * @return 清退记录
     */
    ClearRecordsVO getClearByProcessInstanceId(String instanceId);

    ClearRecordsVO getClearById(String id);

    void addClearRecords(ClearRecordsDTO dto);
}
