package com.ruoyi.srm.supplierBase.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.srm.supplierBase.domain.VO.ChangeRecordsVO;
import com.ruoyi.srm.supplierBase.domain.VO.SupplierBaseVO;
import com.ruoyi.srm.supplierBase.domain.entity.ChangeRecordsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierClearRsEntity;
import com.ruoyi.srm.supplierBase.mapper.SupplierBaseMapper;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: dyw
 * @date: 2023/5/24 16:15
 */
public class SupplierUtil {

    public static List<SupplierBaseVO> transformSupplierEntities(List<SupplierBaseEntity> entities){
        Assert.notNull(entities, "行动项数据为空！");
        List<SupplierBaseVO> vos = new ArrayList<>();
        entities.forEach(entity -> {
            SupplierBaseVO baseVO = new SupplierBaseVO();
            BeanUtils.copyProperties(entity,baseVO);
            vos.add(baseVO);
        });
        return vos;
    }

    public static List<ChangeRecordsVO> transformChangeRecordEntities(List<ChangeRecordsEntity> entities){
        Assert.notNull(entities, "变更记录数据为空！");
        List<ChangeRecordsVO> vos = new ArrayList<>();
        entities.forEach(entity -> {
            ChangeRecordsVO baseVO = new ChangeRecordsVO();
            BeanUtils.copyProperties(entity,baseVO);
            vos.add(baseVO);
        });
        return vos;
    }


}
