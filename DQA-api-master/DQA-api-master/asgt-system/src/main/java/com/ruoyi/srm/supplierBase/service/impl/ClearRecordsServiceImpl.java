package com.ruoyi.srm.supplierBase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.snowflake.SnowFlakeUtil;
import com.ruoyi.srm.supplierBase.constant.SupplierBaseConstant;
import com.ruoyi.srm.supplierBase.domain.DTO.ClearRecordsDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchClearRecordsDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ClearRecordsVO;
import com.ruoyi.srm.supplierBase.domain.VO.SupplierClearRsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ClearRecordsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.PotentialRequirementEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierClearRsEntity;
import com.ruoyi.srm.supplierBase.mapper.ClearRecordsMapper;
import com.ruoyi.srm.supplierBase.mapper.SupplierBaseMapper;
import com.ruoyi.srm.supplierBase.mapper.SupplierClearRsMapper;
import com.ruoyi.srm.supplierBase.service.ClearRecordsService;
import com.ruoyi.srm.supplierBase.service.SupplierClearRsService;
import com.ruoyi.srm.supplierBase.utils.ClearRecordUtil;
import com.ruoyi.srm.supplierBase.utils.SupplierUtil;
import liquibase.pro.packaged.L;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.CredentialsUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: dyw
 * @date: 2023/6/1 13:53
 */
@Service
@Slf4j
public class ClearRecordsServiceImpl extends ServiceImpl<ClearRecordsMapper, ClearRecordsEntity> implements ClearRecordsService {
    @Resource
    private ClearRecordsMapper clearRecordsMapper;

    @Resource
    private SupplierClearRsMapper supplierClearRsMapper;

    @Resource
    private SupplierBaseMapper supplierBaseMapper;

    @Resource
    private SupplierClearRsService supplierClearRsService;

    @Override
    public List<ClearRecordsVO> getClearList(SearchClearRecordsDTO dto) {
        List<ClearRecordsVO> clearRecordsByCondition = clearRecordsMapper.getClearRecordsByCondition(dto);
        if (CollectionUtils.isEmpty(clearRecordsByCondition)){
            return null;
        }
        List<String> recordIds = clearRecordsByCondition.stream().map(ClearRecordsVO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<SupplierClearRsEntity> wrapper = Wrappers.lambdaQuery(SupplierClearRsEntity.class);
        wrapper.eq(SupplierClearRsEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        if (!CollectionUtils.isEmpty(recordIds)){
           wrapper.in(SupplierClearRsEntity::getClearRecordId,recordIds);
        }
        List<SupplierClearRsEntity> supplierClearRsEntities = supplierClearRsMapper.selectList(wrapper);

        if (CollectionUtils.isEmpty(supplierClearRsEntities)){
            return clearRecordsByCondition;
        }
        List<String> supplierIds = supplierClearRsEntities.stream().map(SupplierClearRsEntity::getSupplierId).collect(Collectors.toList());
        Map<String, SupplierBaseEntity> supplierMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(supplierIds)){
            LambdaQueryWrapper<SupplierBaseEntity> wrapper1 = Wrappers.lambdaQuery(SupplierBaseEntity.class);
            wrapper1.in(SupplierBaseEntity::getId,supplierIds);
            List<SupplierBaseEntity> supplierBaseEntities = supplierBaseMapper.selectList(wrapper1);
            supplierMap = supplierBaseEntities.stream().collect(Collectors.toMap(SupplierBaseEntity::getId, item -> item, (key1, key2) -> key2));
        }
        for (ClearRecordsVO clearRecordsVO : clearRecordsByCondition) {
            List<SupplierClearRsVO> suppliers = new ArrayList<>();
            SupplierClearRsVO clearRsVO;
            int i = 0;
            String supplierNames = "";
            for (SupplierClearRsEntity supplierClearRsEntity : supplierClearRsEntities) {
                if (StringUtils.equals(supplierClearRsEntity.getClearRecordId(),clearRecordsVO.getId())){
                    clearRsVO = new SupplierClearRsVO();
                    BeanUtils.copyProperties(supplierClearRsEntity,clearRsVO);
                    clearRsVO.setSupplierName(supplierMap.get(clearRsVO.getSupplierId()).getSupplierName());
                    if (i == 0){
                        supplierNames = supplierMap.get(supplierClearRsEntity.getSupplierId()).getSupplierName();
                    }else {
                        supplierNames = supplierNames.concat("," + supplierMap.get(supplierClearRsEntity.getSupplierId()).getSupplierName());
                    }
                    suppliers.add(clearRsVO);
                    i++;
                }
            }
            clearRecordsVO.setSupplierList(suppliers);
            clearRecordsVO.setSupplierNames(supplierNames);
        }
        return clearRecordsByCondition;
    }

    @Override
    public ClearRecordsVO getClearByProcessInstanceId(String instanceId) {
        if (StringUtils.isEmpty(instanceId)){
            throw new RuntimeException("");
        }
        LambdaQueryWrapper<ClearRecordsEntity> wrapper = Wrappers.lambdaQuery(ClearRecordsEntity.class);
        wrapper.eq(ClearRecordsEntity::getProcessInstanceId,instanceId);
        ClearRecordsEntity entity = clearRecordsMapper.selectOne(wrapper);
        if(ObjectUtils.isEmpty(entity)){
            throw new RuntimeException("清退记录不存在");
        }
        ClearRecordsVO vo = new ClearRecordsVO();
        BeanUtils.copyProperties(entity,vo);
        // 获取供应商列表
        LambdaQueryWrapper<SupplierClearRsEntity> rsWrapper = Wrappers.lambdaQuery(SupplierClearRsEntity.class);
        rsWrapper.eq(SupplierClearRsEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE)
                .eq(SupplierClearRsEntity::getClearRecordId,entity.getId());
        List<SupplierClearRsEntity> supplierClearRsEntities = supplierClearRsMapper.selectList(rsWrapper);
        if (CollectionUtils.isEmpty(supplierClearRsEntities)){
            throw new RuntimeException("未绑定供应商");
        }
        List<SupplierClearRsVO> suppliers = ClearRecordUtil.transformSupplierEntities(supplierClearRsEntities);
        if (CollectionUtils.isEmpty(suppliers)){
            throw new RuntimeException("未绑定供应商");
        }
        // 获取供应商名称
        List<String> supplierIds = supplierClearRsEntities.stream().map(SupplierClearRsEntity::getSupplierId).collect(Collectors.toList());
        Map<String, SupplierBaseEntity> supplierMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(supplierIds)){
            LambdaQueryWrapper<SupplierBaseEntity> supplierWrapper = Wrappers.lambdaQuery(SupplierBaseEntity.class);
            supplierWrapper.in(SupplierBaseEntity::getId,supplierIds);
            List<SupplierBaseEntity> supplierBaseEntities = supplierBaseMapper.selectList(supplierWrapper);
            supplierMap = supplierBaseEntities.stream().collect(Collectors.toMap(SupplierBaseEntity::getId, item -> item, (key1, key2) -> key2));
        }
        for (SupplierClearRsVO rsVO:suppliers){
            rsVO.setSupplierName(supplierMap.get(rsVO.getSupplierId()).getSupplierName());
        }
        vo.setSupplierList(suppliers);
        return vo;
    }

    @Override
    public ClearRecordsVO getClearById(String id) {
        LambdaQueryWrapper<SupplierClearRsEntity>  wrapper = Wrappers.lambdaQuery(SupplierClearRsEntity.class);
        wrapper.eq(SupplierClearRsEntity::getClearRecordId,id).eq(SupplierClearRsEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE);
        List<SupplierClearRsEntity> supplierClearRsEntities = supplierClearRsMapper.selectList(wrapper);
        ClearRecordsVO clearRecordsVO = new ClearRecordsVO();
        if (CollectionUtils.isEmpty(supplierClearRsEntities)){
            return clearRecordsVO;
        }
        List<String> supplierIds = supplierClearRsEntities.stream().map(SupplierClearRsEntity::getSupplierId).collect(Collectors.toList());
        LambdaQueryWrapper<SupplierBaseEntity> wrapper1 = Wrappers.lambdaQuery(SupplierBaseEntity.class);
        wrapper1.in(SupplierBaseEntity::getId,supplierIds);
        List<SupplierBaseEntity> supplierBaseEntities = supplierBaseMapper.selectList(wrapper1);
        Map<String, SupplierBaseEntity> supplierMap = supplierBaseEntities.stream().collect(Collectors.toMap(SupplierBaseEntity::getId, item -> item, (key1, key2) -> key2));
        List<SupplierClearRsVO> supplierClearRsVOS = ClearRecordUtil.transformSupplierEntities(supplierClearRsEntities);
        List<SupplierClearRsVO> clearRsVOS = supplierClearRsVOS.stream().peek(item -> item.setSupplierName(supplierMap.get(item.getSupplierId()).getSupplierName())).collect(Collectors.toList());
        ClearRecordsEntity clearRecordsEntity = clearRecordsMapper.selectById(id);

        BeanUtils.copyProperties(clearRecordsEntity,clearRecordsVO);
        clearRecordsVO.setSupplierList(clearRsVOS);
        return clearRecordsVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addClearRecords(ClearRecordsDTO dto) {
        ClearRecordsEntity clearRecordsEntity = new ClearRecordsEntity();
        BeanUtils.copyProperties(dto,clearRecordsEntity);
        List<SupplierClearRsEntity> supplierList = dto.getSupplierIdList();
        String id = SnowFlakeUtil.snowFlakeId();
        clearRecordsEntity.setId(id);
        supplierList = supplierList.stream().peek(supplierClearRsEntity -> supplierClearRsEntity.setClearRecordId(id))
                .peek(supplierClearRsEntity -> supplierClearRsEntity.setId(SnowFlakeUtil.snowFlakeId()))
                .collect(Collectors.toList());
        clearRecordsMapper.insert(clearRecordsEntity);
        supplierClearRsService.saveBatch(supplierList);
    }
}
