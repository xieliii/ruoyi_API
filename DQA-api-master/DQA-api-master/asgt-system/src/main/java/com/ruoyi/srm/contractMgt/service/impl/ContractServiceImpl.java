package com.ruoyi.srm.contractMgt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.snowflake.SnowFlakeUtil;
import com.ruoyi.srm.contractMgt.constant.ContractConstant;
import com.ruoyi.srm.contractMgt.domain.DTO.ContractDTO;
import com.ruoyi.srm.contractMgt.domain.VO.ContractRsVO;
import com.ruoyi.srm.contractMgt.domain.entity.ContractEntity;
import com.ruoyi.srm.contractMgt.domain.entity.ContractRsProductEntity;
import com.ruoyi.srm.contractMgt.utils.ContractUtil;
import com.ruoyi.srm.supplierBase.constant.SupplierBaseConstant;
import com.ruoyi.srm.supplierBase.domain.entity.ProductsInfoEntity;
import com.ruoyi.srm.contractMgt.mapper.ContractMapper;
import com.ruoyi.srm.contractMgt.mapper.ContractRsProductMapper;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.mapper.ProductsInfoMapper;
import com.ruoyi.srm.contractMgt.service.ContractRsProductService;
import com.ruoyi.srm.contractMgt.service.ContractService;
import com.ruoyi.srm.supplierBase.mapper.SupplierBaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omelette
 */
@Service
@Slf4j
public class ContractServiceImpl extends ServiceImpl<ContractMapper, ContractEntity> implements ContractService {
    @Resource
    private ContractMapper contractMapper;

    @Resource
    private ContractRsProductService contractRsProductService;

    @Resource
    private ContractRsProductMapper contractRsProductMapper;

    @Resource
    private ProductsInfoMapper productsInfoMapper;

    @Resource
    private SupplierBaseMapper supplierBaseMapper;

    @Override
    public IPage<ContractRsVO> getContractByPage(ContractDTO dto) {
        Integer pageNum = dto.getPageNum();
        Integer pageSize = dto.getPageSize();
        if (pageNum <= ContractConstant.ZERO){
            pageNum = ContractConstant.PAGE_NUM;
        }
        if (pageSize <= ContractConstant.ZERO){
            pageSize = ContractConstant.PAGE_SIZE;
        }
        Page<ContractEntity> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<ContractEntity> wrapper = Wrappers.lambdaQuery(ContractEntity.class)
                .eq(ContractEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE);
        if (StringUtils.isNotEmpty(dto.getName())){
            wrapper.like(ContractEntity::getName,dto.getName());
        }
        if (StringUtils.isNotEmpty(dto.getCode())){
            wrapper.like(ContractEntity::getCode,dto.getCode());
        }
        if (dto.getStatus()!=null){
            wrapper.eq(ContractEntity::getStatus,dto.getStatus());
        }
        IPage<ContractEntity> contractEntityPage = contractMapper.selectPage(page, wrapper);
        List<ContractEntity> records = contractEntityPage.getRecords();
        if (records.size()>0){
            List<ContractRsVO> contractRsVos = ContractUtil.transformContractEntities(records);
            // 供应商id -> 供应商名称
            List<String> supplierIds = contractRsVos.stream().map(ContractRsVO::getSupplierId).collect(Collectors.toList());
            List<SupplierBaseEntity> supplierBaseEntities= supplierBaseMapper.selectBatchIds(supplierIds);
            HashMap<String,String> supplierIdAndNameMap = new HashMap<>(supplierIds.size());
            for (SupplierBaseEntity entity:supplierBaseEntities){
                supplierIdAndNameMap.put(entity.getId(),entity.getSupplierName());
            }
            for (ContractRsVO vo:contractRsVos){
                vo.setSupplierName(supplierIdAndNameMap.get(vo.getSupplierId()));
            }
            IPage<ContractRsVO> result = new Page<>(pageNum,pageSize);
            result.setRecords(contractRsVos);
            result.setCurrent(contractEntityPage.getCurrent());
            result.setPages(contractEntityPage.getPages());
            result.setSize(contractEntityPage.getSize());
            result.setTotal(contractEntityPage.getTotal());
            return result;
        }
        else {
            IPage<ContractRsVO> result = new Page<>(pageNum,pageSize);
            result.setTotal(ContractConstant.ZERO);
            return result;
        }
    }

    @Override
    public ContractRsVO getContractById(String id) {
        LambdaQueryWrapper<ContractEntity> wrapper = Wrappers.lambdaQuery(ContractEntity.class);
        wrapper.eq(ContractEntity::getId,id);
        ContractEntity entity = contractMapper.selectOne(wrapper);
        ContractRsVO contractRsVO = new ContractRsVO();
        BeanUtils.copyProperties(entity,contractRsVO);
        // 拿到产品数据
        LambdaQueryWrapper<ContractRsProductEntity> rsWrapper = Wrappers.lambdaQuery(ContractRsProductEntity.class);
        rsWrapper.eq(ContractRsProductEntity::getContractId,id);
        List<ContractRsProductEntity> productList = contractRsProductMapper.selectList(rsWrapper);
        List<String> productIds = productList.stream().map(ContractRsProductEntity::getProductId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(productIds)){
            throw new RuntimeException("该合同不存在产品");
        }
        List<ProductsInfoEntity> productsInfo =  productsInfoMapper.selectBatchIds(productIds);
        contractRsVO.setProductList(productsInfo);
        return contractRsVO;
    }

    @Override
    public Boolean addContract(ContractDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            throw new RuntimeException("新增数据为空");
        }
        // 合同
        ContractEntity entity = new ContractEntity();
        BeanUtils.copyProperties(dto,entity);
        String id = SnowFlakeUtil.snowFlakeId();
        entity.setId(id);
        contractMapper.insert(entity);
        // 产品
        List<String> productIds = dto.getProductIds();
        if (CollectionUtils.isEmpty(productIds)){
            throw new RuntimeException("绑定产品为空");
        };
        List<ContractRsProductEntity> rsList = new ArrayList<>();
        productIds.forEach(productId->{
            String rsId = SnowFlakeUtil.snowFlakeId();
            ContractRsProductEntity rsEntity = new ContractRsProductEntity();
            rsEntity.setProductId(productId);
            rsEntity.setSupplierId(dto.getSupplierId());
            rsEntity.setId(rsId);
            rsEntity.setContractId(id);
            rsList.add(rsEntity);
        });
        return contractRsProductService.saveBatch(rsList);
    }

    @Override
    public Integer updateContract(ContractDTO dto) {
        ContractEntity contractEntity = new ContractEntity();
        BeanUtils.copyProperties(dto,contractEntity);
        // 产品是否需要更新
        List<String> productIds = dto.getProductIds();
        return contractMapper.updateById(contractEntity);
    }

    @Override
    public Integer delContract(String id) {
        if (StringUtils.isEmpty(id)){
            throw new RuntimeException("合同不存在");
        }
        return contractMapper.deleteById(id);
    }

    @Override
    public Integer batchDelContract(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)){
            throw new RuntimeException("列表为空");
        }
        return contractMapper.deleteBatchIds(ids);
    }
}
