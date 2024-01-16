package com.ruoyi.srm.supplierBase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchChangeLogDTO;
import com.ruoyi.srm.supplierBase.domain.entity.ChangeRecordsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: dyw
 * @date: 2023/5/25 14:38
 */
public interface ChangeRecordsMapper extends BaseMapper<ChangeRecordsEntity> {

    List<ChangeRecordsEntity> getChangeLogByCondition(@Param("dto") SearchChangeLogDTO dto);
}
