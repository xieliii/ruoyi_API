package com.ruoyi.srm.supplierBase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.snowflake.SnowFlakeUtil;
import com.ruoyi.srm.supplierBase.constant.ErrorConstant;
import com.ruoyi.srm.supplierBase.constant.SupplierBaseConstant;
import com.ruoyi.srm.supplierBase.domain.DTO.PotentialRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.VO.PotentialReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.PotentialRequirementEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierListEntity;
import com.ruoyi.srm.supplierBase.mapper.PotentialRequirementMapper;
import com.ruoyi.srm.supplierBase.mapper.SupplierListMapper;
import com.ruoyi.srm.supplierBase.service.PotentialRequirementService;
import com.ruoyi.srm.supplierBase.service.SupplierListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: parus
 * @date: 2023/6/5 16:15
 */
@Service
@Slf4j
public class PotentialRequirementServiceImpl extends ServiceImpl<PotentialRequirementMapper,PotentialRequirementEntity> implements PotentialRequirementService {

    @Resource
    private PotentialRequirementMapper potentialRequirementMapper;

    @Resource
    private SupplierListService supplierListService;

    @Resource
    private SupplierListMapper supplierListMapper;

    @Resource
    private SupplierBaseServiceImpl supplierBaseService;

    @Override
    public IPage<PotentialRequirementEntity> getPotentialReqByPage(Integer pageNum, Integer pageSize) {
        if (pageNum<=0){
            pageNum = 1;
        }
        if (pageSize<=0){
            pageSize = 10;
        }
        Page<PotentialRequirementEntity> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<PotentialRequirementEntity> wrapper = Wrappers.lambdaQuery(PotentialRequirementEntity.class);
        wrapper.eq(PotentialRequirementEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE);
        return potentialRequirementMapper.selectPage(page, wrapper);
    }

    @Override
    public PotentialReqVO getPotentialByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)){
            throw new RuntimeException("流程实例id为空，无法获取业务数据！");
        }
        LambdaQueryWrapper<PotentialRequirementEntity> wrapper = Wrappers.lambdaQuery(PotentialRequirementEntity.class);
        wrapper.eq(PotentialRequirementEntity::getProcessInstanceId,processInstanceId);
        PotentialRequirementEntity entity = potentialRequirementMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(entity)){
            throw new RuntimeException("潜在入册需求申请不存在");
        }
        PotentialReqVO res = new PotentialReqVO();
        BeanUtils.copyProperties(entity,res);
        // 获取供应商列表
        LambdaQueryWrapper<SupplierListEntity> supplierListWrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
        supplierListWrapper.eq(SupplierListEntity::getPotentialReqId,entity.getId());
        List<SupplierListEntity> supplierList = supplierListMapper.selectList(supplierListWrapper);
        // 查询供应商信息
        List<String> ids = supplierList.stream().map(SupplierListEntity::getSupplierId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)){
            throw new RuntimeException("未绑定供应商");
        }
        List<SupplierBaseEntity> baseEntity = supplierBaseService.listByIds(ids);
        res.setSupplierList(baseEntity);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPotentialReq(PotentialRequirementDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            throw new RuntimeException(ErrorConstant.NOT_EXIST);
        }
        PotentialRequirementEntity entity = new PotentialRequirementEntity();
        BeanUtils.copyProperties(dto,entity);
        String id = SnowFlakeUtil.snowFlakeId();
        entity.setId(id);
        List<SupplierListEntity> supplierList = dto.getSupplierList();
        List<SupplierListEntity> temp = new ArrayList<>();
        if (!CollectionUtils.isEmpty(supplierList)){
            for (SupplierListEntity listEntity : supplierList) {
                if (!(StringUtils.isEmpty(listEntity.getSupplierId())
                        //暂未启用RFI RFP RFQ
//                        StringUtils.isEmpty(listEntity.getAddress())
//                        && StringUtils.isEmpty(listEntity.getSupplierCode())
//                        && StringUtils.isEmpty(listEntity.getRfiId())
//                        && StringUtils.isEmpty(listEntity.getRfpId()) && StringUtils.isEmpty(listEntity.getRfqId())
                )
                ){
                    temp.add(listEntity);
                }
            }
            temp = temp.stream().peek(supplierListEntity -> supplierListEntity.setPotentialReqId(id))
                    .peek(supplierListEntity -> supplierListEntity.setId(SnowFlakeUtil.snowFlakeId()))
                    .collect(Collectors.toList());
            supplierListService.saveBatch(temp);
        }
        potentialRequirementMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePotentialReq(PotentialRequirementDTO dto) {
        PotentialRequirementEntity entity = new PotentialRequirementEntity();
        BeanUtils.copyProperties(dto,entity);
        potentialRequirementMapper.updateById(entity);
        LambdaQueryWrapper<SupplierListEntity> wrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
        wrapper.eq(SupplierListEntity::getPotentialReqId,dto.getId());
        supplierListMapper.delete(wrapper);
        for (SupplierListEntity supplier: dto.getSupplierList()){
            String id = SnowFlakeUtil.snowFlakeId();
            supplier.setId(id);
        }
        supplierListService.saveBatch(dto.getSupplierList());
    }

    @Override
    public PotentialRequirementDTO getPotentialById(String id) {
        PotentialRequirementDTO result = new PotentialRequirementDTO();
        PotentialRequirementEntity entity = potentialRequirementMapper.selectById(id);
        BeanUtils.copyProperties(entity,result);
        LambdaQueryWrapper<SupplierListEntity> wrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
        wrapper.eq(SupplierListEntity::getPotentialReqId,id);
        List<SupplierListEntity> supplierListEntities = supplierListMapper.selectList(wrapper);
        result.setSupplierList(supplierListEntities);
        return result;
    }
}
