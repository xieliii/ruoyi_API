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
import com.ruoyi.srm.supplierBase.domain.DTO.ApprovedRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.VO.WorkflowRsApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.*;
import com.ruoyi.srm.supplierBase.mapper.ApprovedRequirementMapper;
import com.ruoyi.srm.supplierBase.mapper.SupplierBaseMapper;
import com.ruoyi.srm.supplierBase.mapper.SupplierListMapper;
import com.ruoyi.srm.supplierBase.mapper.SupplierPurchaseDetailMapper;
import com.ruoyi.srm.supplierBase.service.ApprovedRequirementService;
import com.ruoyi.srm.supplierBase.service.SupplierBaseService;
import com.ruoyi.srm.supplierBase.service.SupplierListService;
import com.ruoyi.srm.supplierBase.service.SupplierPurchaseDetailService;
import com.ruoyi.workflow.constant.ProcessConstant;
import liquibase.pro.packaged.L;
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
 * @date: 2023/6/5 16:14
 */
@Service
@Slf4j
public class ApprovedRequirementServiceImpl extends ServiceImpl<ApprovedRequirementMapper, ApprovedRequirementEntity> implements ApprovedRequirementService {

    @Resource
    private ApprovedRequirementMapper approvedRequirementMapper;

    @Resource
    private SupplierPurchaseDetailMapper supplierPurchaseDetailMapper;

    @Resource
    private SupplierPurchaseDetailService supplierPurchaseDetailService;

    @Resource
    private SupplierListMapper supplierListMapper;

    @Resource
    private SupplierListService supplierListService;

    @Resource
    private SupplierBaseMapper supplierBaseMapper;

    @Override
    public IPage<ApprovedRequirementEntity> getApprovedReqByPage(Integer pageNum, Integer pageSize) {
        if (pageNum<=0){
            pageNum = 1;
        }
        if (pageSize<=0){
            pageSize = 10;
        }
        Page<ApprovedRequirementEntity> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<ApprovedRequirementEntity> wrapper = Wrappers.lambdaQuery(ApprovedRequirementEntity.class);
        wrapper.eq(ApprovedRequirementEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE);
        return approvedRequirementMapper.selectPage(page, wrapper);
    }

    /**
     * 根据流程实例id获取信息
     *
     * @param processInstanceId 流程实例id
     * @return 获批需求
     */
    @Override
    public WorkflowRsApprovedReqVO getApprovedByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)){
            throw new RuntimeException("");
        }
        LambdaQueryWrapper<ApprovedRequirementEntity> wrapper = Wrappers.lambdaQuery(ApprovedRequirementEntity.class);
        wrapper.eq(ApprovedRequirementEntity::getProcessInstanceId,processInstanceId);
        ApprovedRequirementEntity entity = approvedRequirementMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(entity)){
            throw new RuntimeException("获批入册需求申请不存在");
        }
        WorkflowRsApprovedReqVO res = new WorkflowRsApprovedReqVO();
//        ApprovedReqVO res = new ApprovedReqVO();
        BeanUtils.copyProperties(entity,res);
        // 获取采购明细列表
        LambdaQueryWrapper<SupplierPurchaseDetailEntity> purchaseListWrapper = Wrappers.lambdaQuery(SupplierPurchaseDetailEntity.class);
        purchaseListWrapper.eq(SupplierPurchaseDetailEntity::getApprovedRequirementId,entity.getId());
        List<SupplierPurchaseDetailEntity> purchaseList = supplierPurchaseDetailMapper.selectList(purchaseListWrapper);
        res.setPurchaseList(purchaseList);
        // 获取供应商列表
        LambdaQueryWrapper<SupplierListEntity> supplierListWrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
        supplierListWrapper.eq(SupplierListEntity::getApprovedRequirementId,entity.getId());
        List<SupplierListEntity> supplierList = supplierListMapper.selectList(supplierListWrapper);
        res.setSupplierList(supplierList);
//        List<String> supplierIds = supplierList.stream().map(SupplierListEntity::getSupplierId).collect(Collectors.toList());
//        if (CollectionUtils.isNotEmpty(supplierIds)){
//            List<SupplierBaseEntity> baseEntities = supplierBaseMapper.selectBatchIds(supplierIds);
//            res.setSupplierList(baseEntities);
//        }
        return res;
    }

    @Override
    public WorkflowRsApprovedReqVO getApprovedReqByProcess(String id, String processKey) {
        if (StringUtils.isEmpty(id)||StringUtils.isEmpty(processKey)){
            throw new RuntimeException("流程实例不存在");
        }

        LambdaQueryWrapper<ApprovedRequirementEntity> wrapper = Wrappers.lambdaQuery(ApprovedRequirementEntity.class);
        WorkflowRsApprovedReqVO res = new WorkflowRsApprovedReqVO();

        if (processKey.equals(ProcessConstant.APPROVED_PROCESS_KEY)){
            return this.getApprovedByProcessInstanceId(id);
        }
        else if (processKey.equals(ProcessConstant.RFI_PROCESS_KEY)){

            wrapper.eq(ApprovedRequirementEntity::getRfiProcessInstanceId,id);
            ApprovedRequirementEntity entity = approvedRequirementMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(entity)){
                throw new RuntimeException("获批入册需求申请不存在");
            }
            BeanUtils.copyProperties(entity,res);
            // 获取采购明细列表
            LambdaQueryWrapper<SupplierPurchaseDetailEntity> purchaseListWrapper = Wrappers.lambdaQuery(SupplierPurchaseDetailEntity.class);
            purchaseListWrapper.eq(SupplierPurchaseDetailEntity::getApprovedRequirementId,entity.getId());
            List<SupplierPurchaseDetailEntity> purchaseList = supplierPurchaseDetailMapper.selectList(purchaseListWrapper);
            res.setPurchaseList(purchaseList);
            // 获取供应商列表
            LambdaQueryWrapper<SupplierListEntity> supplierListWrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
            supplierListWrapper.eq(SupplierListEntity::getApprovedRequirementId,entity.getId());
            List<SupplierListEntity> supplierList = supplierListMapper.selectList(supplierListWrapper);
            res.setSupplierList(supplierList);
            return res;
        }
        else if (processKey.equals(ProcessConstant.RFP_PROCESS_KEY)){
            wrapper.eq(ApprovedRequirementEntity::getRfpProcessInstanceId,id);
            ApprovedRequirementEntity entity = approvedRequirementMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(entity)){
                throw new RuntimeException("获批入册需求申请不存在");
            }
            BeanUtils.copyProperties(entity,res);
            // 获取采购明细列表
            LambdaQueryWrapper<SupplierPurchaseDetailEntity> purchaseListWrapper = Wrappers.lambdaQuery(SupplierPurchaseDetailEntity.class);
            purchaseListWrapper.eq(SupplierPurchaseDetailEntity::getApprovedRequirementId,entity.getId());
            List<SupplierPurchaseDetailEntity> purchaseList = supplierPurchaseDetailMapper.selectList(purchaseListWrapper);
            res.setPurchaseList(purchaseList);
            // 获取供应商列表
            LambdaQueryWrapper<SupplierListEntity> supplierListWrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
            supplierListWrapper.eq(SupplierListEntity::getApprovedRequirementId,entity.getId());
            List<SupplierListEntity> supplierList = supplierListMapper.selectList(supplierListWrapper);
            res.setSupplierList(supplierList);
            return res;
        }
        else if (processKey.equals(ProcessConstant.RFQ_PROCESS_KEY)){
            wrapper.eq(ApprovedRequirementEntity::getRfqProcessInstanceId,id);
            ApprovedRequirementEntity entity = approvedRequirementMapper.selectOne(wrapper);
            if (ObjectUtils.isEmpty(entity)){
                throw new RuntimeException("获批入册需求申请不存在");
            }
            BeanUtils.copyProperties(entity,res);
            // 获取采购明细列表
            LambdaQueryWrapper<SupplierPurchaseDetailEntity> purchaseListWrapper = Wrappers.lambdaQuery(SupplierPurchaseDetailEntity.class);
            purchaseListWrapper.eq(SupplierPurchaseDetailEntity::getApprovedRequirementId,entity.getId());
            List<SupplierPurchaseDetailEntity> purchaseList = supplierPurchaseDetailMapper.selectList(purchaseListWrapper);
            res.setPurchaseList(purchaseList);
            // 获取供应商列表
            LambdaQueryWrapper<SupplierListEntity> supplierListWrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
            supplierListWrapper.eq(SupplierListEntity::getApprovedRequirementId,entity.getId());
            List<SupplierListEntity> supplierList = supplierListMapper.selectList(supplierListWrapper);
            res.setSupplierList(supplierList);
            return res;
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addApprovedReq(ApprovedRequirementDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            throw new RuntimeException(ErrorConstant.NOT_EXIST);
        }
        ApprovedRequirementEntity approvedEntity = new ApprovedRequirementEntity();
        BeanUtils.copyProperties(dto,approvedEntity);
        String id = SnowFlakeUtil.snowFlakeId();
        approvedEntity.setId(id);
        List<SupplierPurchaseDetailEntity> purchaseDetailList = dto.getPurchaseDetailList();
        List<SupplierPurchaseDetailEntity> temp = new ArrayList<>();
        if (!CollectionUtils.isEmpty(purchaseDetailList)){
            for(SupplierPurchaseDetailEntity entity : purchaseDetailList){
                if (!(StringUtils.isEmpty(entity.getItemName()))){
                    entity.setApprovedRequirementId(id);
                    temp.add(entity);
                }
            }
            temp = temp.stream().peek(purchaseEntity -> purchaseEntity.setApprovedRequirementId(id))
                    .peek(supplierListEntity -> supplierListEntity.setId(SnowFlakeUtil.snowFlakeId()))
                    .collect(Collectors.toList());
            supplierPurchaseDetailService.saveBatch(temp);
        }
        List<SupplierListEntity> supplierList = dto.getSupplierList();
        if (CollectionUtils.isNotEmpty(supplierList)){
            for (SupplierListEntity supplierEntity:supplierList) {
                String snowFlakeId = SnowFlakeUtil.snowFlakeId();
                supplierEntity.setId(snowFlakeId);
                supplierEntity.setApprovedRequirementId(id);
            }
            supplierListService.saveBatch(supplierList);
        }
        approvedRequirementMapper.insert(approvedEntity);
    }

    @Override
    public void updateApprovedReq(ApprovedRequirementDTO dto) {
        ApprovedRequirementEntity entity = new ApprovedRequirementEntity();
        BeanUtils.copyProperties(dto,entity);
        approvedRequirementMapper.updateById(entity);

        LambdaQueryWrapper<SupplierPurchaseDetailEntity> wrapper = Wrappers.lambdaQuery(SupplierPurchaseDetailEntity.class);
        wrapper.eq(SupplierPurchaseDetailEntity::getApprovedRequirementId,dto.getId());
        supplierPurchaseDetailMapper.delete(wrapper);
        List<SupplierPurchaseDetailEntity> purchaseDetailList = dto.getPurchaseDetailList();
        if (CollectionUtils.isNotEmpty(purchaseDetailList)){
            for (SupplierPurchaseDetailEntity purchase:purchaseDetailList) {
                String purchaseId = SnowFlakeUtil.snowFlakeId();
                purchase.setId(purchaseId);
                purchase.setApprovedRequirementId(dto.getId());
            }
            supplierPurchaseDetailService.saveBatch(dto.getPurchaseDetailList());
        }

        LambdaQueryWrapper<SupplierListEntity> supplierWrapper = Wrappers.lambdaQuery(SupplierListEntity.class);
        supplierWrapper.eq(SupplierListEntity::getApprovedRequirementId,dto.getId());
        supplierListMapper.delete(supplierWrapper);
        List<SupplierListEntity> supplierList = dto.getSupplierList();
        if (CollectionUtils.isNotEmpty(supplierList)){
            for (SupplierListEntity supplier:supplierList){
                String supplierListId = SnowFlakeUtil.snowFlakeId();
                supplier.setApprovedRequirementId(dto.getId());
                supplier.setId(supplierListId);
            }
            supplierListService.saveBatch(supplierList);
        }
    }

    @Override
    public ApprovedRequirementDTO getApprovedById(String id) {
        ApprovedRequirementDTO result = new ApprovedRequirementDTO();
        ApprovedRequirementEntity entity = approvedRequirementMapper.selectById(id);
        BeanUtils.copyProperties(entity,result);
        LambdaQueryWrapper<SupplierPurchaseDetailEntity> wrapper = Wrappers.lambdaQuery(SupplierPurchaseDetailEntity.class);
        wrapper.eq(SupplierPurchaseDetailEntity::getApprovedRequirementId,id);
        List<SupplierPurchaseDetailEntity> purchaseDetailEntities = supplierPurchaseDetailMapper.selectList(wrapper);
        result.setPurchaseDetailList(purchaseDetailEntities);
        return result;
    }
}
