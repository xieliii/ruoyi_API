package com.ruoyi.srm.supplierBase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchClearRecordsDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ClearRecordsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ClearRecordsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: dyw
 * @date: 2023/5/25 16:20
 */
public interface ClearRecordsMapper extends BaseMapper<ClearRecordsEntity> {

    List<ClearRecordsVO> getClearRecordsByCondition(@Param("dto") SearchClearRecordsDTO dto);
}
