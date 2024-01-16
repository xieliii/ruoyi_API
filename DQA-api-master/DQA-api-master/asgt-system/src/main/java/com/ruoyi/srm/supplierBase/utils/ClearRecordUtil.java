package com.ruoyi.srm.supplierBase.utils;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.ruoyi.srm.supplierBase.domain.VO.ClearRecordsVO;
import com.ruoyi.srm.supplierBase.domain.VO.SupplierClearRsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ClearRecordsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierClearRsEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: dyw
 * @date: 2023/6/1 16:56
 */
public class ClearRecordUtil {
    public static List<ClearRecordsVO> transformClearRecordEntities(List<ClearRecordsEntity> entities){
        Assert.notNull(entities, "清退记录不存在");
        List<ClearRecordsVO> vos = new ArrayList<>();
        entities.forEach(entity -> {
            ClearRecordsVO baseVO = new ClearRecordsVO();
            BeanUtils.copyProperties(entity,baseVO);
            vos.add(baseVO);
        });
        return vos;
    }

    public static List<SupplierClearRsVO> transformSupplierEntities(List<SupplierClearRsEntity> entities){
        Assert.notNull(entities, "供应商记录不存在");
        List<SupplierClearRsVO> vos = new ArrayList<>();
        entities.forEach(entity -> {
            SupplierClearRsVO baseVO = new SupplierClearRsVO();
            BeanUtils.copyProperties(entity,baseVO);
            vos.add(baseVO);
        });
        return vos;
    }
}
