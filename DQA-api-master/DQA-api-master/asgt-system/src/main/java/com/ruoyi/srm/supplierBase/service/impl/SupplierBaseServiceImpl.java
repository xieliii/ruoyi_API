package com.ruoyi.srm.supplierBase.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.snowflake.SnowFlakeUtil;
import com.ruoyi.srm.supplierBase.constant.SupplierBaseConstant;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchSuppliersDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.SupplierBaseDTO;
import com.ruoyi.srm.supplierBase.domain.VO.CategorySumVO;
import com.ruoyi.srm.supplierBase.domain.VO.SupplierBaseVO;
import com.ruoyi.srm.supplierBase.domain.entity.AuthenticationInfoEntity;
import com.ruoyi.srm.supplierBase.domain.entity.BusinessInfoEntity;
import com.ruoyi.srm.supplierBase.domain.entity.ContactsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.ProductsInfoEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.mapper.AuthenticationInfoMapper;
import com.ruoyi.srm.supplierBase.mapper.BusinessInfoMapper;
import com.ruoyi.srm.supplierBase.mapper.ContactsMapper;
import com.ruoyi.srm.supplierBase.mapper.PotentialRequirementMapper;
import com.ruoyi.srm.supplierBase.mapper.ProductsInfoMapper;
import com.ruoyi.srm.supplierBase.mapper.SupplierBaseMapper;
import com.ruoyi.srm.supplierBase.service.AuthenticationInfoService;
import com.ruoyi.srm.supplierBase.service.BusinessInfoService;
import com.ruoyi.srm.supplierBase.service.ContactsService;
import com.ruoyi.srm.supplierBase.service.ProductsInfoService;
import com.ruoyi.srm.supplierBase.service.SupplierBaseService;
import com.ruoyi.srm.supplierBase.utils.SupplierUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: dyw
 * @date: 2023/5/24 11:44
 */
@Service
@Slf4j
public class SupplierBaseServiceImpl extends ServiceImpl<SupplierBaseMapper, SupplierBaseEntity> implements SupplierBaseService {
    @Resource
    private SupplierBaseMapper supplierBaseMapper;

    @Resource
    private AuthenticationInfoMapper authenticationInfoMapper;

    @Resource
    private AuthenticationInfoService authenticationInfoService;

    @Resource
    private BusinessInfoMapper businessInfoMapper;

    @Resource
    private BusinessInfoService businessInfoService;

    @Resource
    private ContactsMapper contactsMapper;

    @Resource
    private ContactsService contactsService;

    @Resource
    private PotentialRequirementMapper potentialRequirementMapper;

    @Resource
    private ProductsInfoMapper productsInfoMapper;

    @Resource
    private ProductsInfoService productsInfoService;

    @Override
    public CategorySumVO getCategorySum() {
        LambdaQueryWrapper<SupplierBaseEntity> wrapper = Wrappers.lambdaQuery(SupplierBaseEntity.class);
        wrapper.eq(SupplierBaseEntity::getCategory,SupplierBaseConstant.SOURCE).eq(SupplierBaseEntity::getAbleFlag,SupplierBaseConstant.IS_ABLE);
        Integer source = supplierBaseMapper.selectCount(wrapper);
        LambdaQueryWrapper<SupplierBaseEntity> wrapper2 = Wrappers.lambdaQuery(SupplierBaseEntity.class);
        wrapper2.eq(SupplierBaseEntity::getCategory,SupplierBaseConstant.POTENTIAL);
        Integer potential = supplierBaseMapper.selectCount(wrapper2);
        LambdaQueryWrapper<SupplierBaseEntity> wrapper3 = Wrappers.lambdaQuery(SupplierBaseEntity.class);
        wrapper3.eq(SupplierBaseEntity::getCategory,SupplierBaseConstant.APPROVED);
        Integer approved = supplierBaseMapper.selectCount(wrapper3);
        CategorySumVO categorySumVO = new CategorySumVO();
        categorySumVO.setSourceSum(source);
        categorySumVO.setApprovedSum(approved);
        categorySumVO.setPotentialSum(potential);
        return categorySumVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SupplierBaseEntity> listAllSupplierByPage(SearchSuppliersDTO dto) {
        Integer kind = dto.getKind();
        Integer category = dto.getCategory();
        LambdaQueryWrapper<SupplierBaseEntity> wrapper = Wrappers.lambdaQuery(SupplierBaseEntity.class);
        if (kind.equals(SupplierBaseConstant.SOURCE)){
            wrapper.eq(SupplierBaseEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE)
                    .eq(SupplierBaseEntity::getAbleFlag,SupplierBaseConstant.IS_ABLE);
            if (category != null){
                wrapper.eq(SupplierBaseEntity::getCategory,category);
            }
        }else if (kind.equals(SupplierBaseConstant.POTENTIAL)){
            if (category == null || category.equals(SupplierBaseConstant.SOURCE)){
                wrapper.eq(SupplierBaseEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE).ne(SupplierBaseEntity::getCategory,SupplierBaseConstant.SOURCE);
            }else {
                wrapper.eq(SupplierBaseEntity::getDeleteFlag, SupplierBaseConstant.IS_NOT_DELETE).ne(SupplierBaseEntity::getCategory,SupplierBaseConstant.SOURCE)
                        .eq(SupplierBaseEntity::getCategory,category);
            }
        }else if (kind.equals(SupplierBaseConstant.APPROVED)){
            wrapper.eq(SupplierBaseEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE).eq(SupplierBaseEntity::getCategory,kind);
        }

        //供应商编号
        String code = dto.getCode();
        //供应商名称
        String name = dto.getSupplierName();
        //公司性质
        Integer companyNature = dto.getCompanyNature();
        if (!StringUtils.isEmpty(code)){
            wrapper.like(SupplierBaseEntity::getCode,code);
        }
        if (!StringUtils.isEmpty(name)){
            wrapper.like(SupplierBaseEntity::getSupplierName,name);
        }
        if (companyNature != null){
            wrapper.eq(SupplierBaseEntity::getCompanyNature,companyNature);
        }
        List<SupplierBaseEntity> supplierBaseEntities = supplierBaseMapper.selectList(wrapper);

        return supplierBaseEntities;
    }

    @Override
    public List<SupplierBaseEntity> listAllSupplierByKind(Integer kind) {
        LambdaQueryWrapper<SupplierBaseEntity> wrapper = Wrappers.lambdaQuery(SupplierBaseEntity.class);
        wrapper.eq(SupplierBaseEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        if (kind != null){
            if(kind.equals(SupplierBaseConstant.SOURCE)){
             wrapper.eq(SupplierBaseEntity::getCategory,SupplierBaseConstant.SOURCE).eq(SupplierBaseEntity::getAbleFlag,SupplierBaseConstant.IS_ABLE);
            }else if (kind.equals(SupplierBaseConstant.POTENTIAL)){
                wrapper.eq(SupplierBaseEntity::getCategory,SupplierBaseConstant.POTENTIAL);
            }else if (kind.equals(SupplierBaseConstant.APPROVED)){
                wrapper.eq(SupplierBaseEntity::getCategory,SupplierBaseConstant.APPROVED);
            }
        }
        return supplierBaseMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSupplier(SupplierBaseDTO supplierBaseDTO) {
        String id = SnowFlakeUtil.snowFlakeId();
        SupplierBaseEntity entity = new SupplierBaseEntity();
        entity.setId(id);
        entity.setCategory(SupplierBaseConstant.SOURCE);
        BeanUtils.copyProperties(supplierBaseDTO,entity);
        supplierBaseMapper.insert(entity);
        List<AuthenticationInfoEntity> authenticationInfoEntities = supplierBaseDTO.getAuthenticationInfoEntities();
        List<AuthenticationInfoEntity> list1 = new ArrayList<>();
        for (AuthenticationInfoEntity authenticationInfoEntity : authenticationInfoEntities) {
            if (!(StringUtils.isEmpty(authenticationInfoEntity.getStandard())
                    &&StringUtils.isEmpty(authenticationInfoEntity.getCertificateNo())
                    &&StringUtils.isEmpty(authenticationInfoEntity.getCertificationScope()))){
                list1.add(authenticationInfoEntity);
            }
        }

        if (!CollectionUtils.isEmpty(list1)){
            List<AuthenticationInfoEntity> authenticationList = list1.stream()
                    .filter(authenticationInfoEntity -> !ObjectUtils.isEmpty(authenticationInfoEntity))
                    .peek(authenticationInfoEntity -> authenticationInfoEntity.setSupplierId(id))
                    .peek(authenticationInfoEntity -> authenticationInfoEntity.setId(SnowFlakeUtil.snowFlakeId()))
                    .collect(Collectors.toList());
            authenticationInfoService.saveBatch(authenticationList);
        }
        List<ContactsEntity> contactsEntityList = supplierBaseDTO.getContactsEntities();
        List<ContactsEntity> list2 = new ArrayList<>();
        for (ContactsEntity contactsEntity : contactsEntityList) {
            if (!StringUtils.isEmpty(contactsEntity.getName())
                    ||!StringUtils.isEmpty(contactsEntity.getPosition())
                    ||!StringUtils.isEmpty(contactsEntity.getPhone())
                    ||!StringUtils.isEmpty(contactsEntity.getTelephone())
                    ||!StringUtils.isEmpty(contactsEntity.getEmail())
                    ||!StringUtils.isEmpty(contactsEntity.getFax())){
                list2.add(contactsEntity);
            }
        }
        if (!CollectionUtils.isEmpty(list2)){
            List<ContactsEntity> contactsEntities = list2.stream()
                    .filter(contactsEntity -> !ObjectUtils.isEmpty(contactsEntity))
                    .peek(contactsEntity -> contactsEntity.setSupplierId(id))
                    .peek(contactsEntity -> contactsEntity.setId(SnowFlakeUtil.snowFlakeId()))
                    .collect(Collectors.toList());
            contactsService.saveBatch(contactsEntities);
        }
        List<BusinessInfoEntity> infoEntityList = supplierBaseDTO.getBusinessInfoEntities();
        List<BusinessInfoEntity> list3 = new ArrayList<>();
        for (BusinessInfoEntity businessEntity : infoEntityList) {
            if (!StringUtils.isEmpty(businessEntity.getCustomer())
                    ||!StringUtils.isEmpty(businessEntity.getAircraftProjects())
                    ||!StringUtils.isEmpty(businessEntity.getSubcontractor())){
                list3.add(businessEntity);
            }
        }
        if (!CollectionUtils.isEmpty(list3)){
            List<BusinessInfoEntity> businessInfoEntities = list3.stream()
                    .peek(businessInfoEntity -> businessInfoEntity.setSupplierId(id))
                    .peek(businessInfoEntity -> businessInfoEntity.setId(SnowFlakeUtil.snowFlakeId()))
                    .collect(Collectors.toList());
            businessInfoService.saveBatch(businessInfoEntities);
        }
        List<ProductsInfoEntity> productsInfoEntityList = supplierBaseDTO.getProductsInfoEntities();
        List<ProductsInfoEntity> list4 = new ArrayList<>();
        for (ProductsInfoEntity productsEntity : productsInfoEntityList) {
            if (!StringUtils.isEmpty(productsEntity.getName())
                    ||!StringUtils.isEmpty(productsEntity.getProductModel())
                    ||!StringUtils.isEmpty(productsEntity.getProductUnique())
                    ||productsEntity.getSupplierType()!= null
                    ||productsEntity.getFileId()!= null){
                list4.add(productsEntity);
            }
        }
        if (!CollectionUtils.isEmpty(list4)){
            List<ProductsInfoEntity> productsInfoEntities = list4.stream()
                    .filter(productsInfoEntity -> !ObjectUtils.isEmpty(productsInfoEntity))
                    .peek(productsInfoEntity -> productsInfoEntity.setSupplierId(id))
                    .peek(productsInfoEntity -> productsInfoEntity.setId(SnowFlakeUtil.snowFlakeId()))
                    .collect(Collectors.toList());
            productsInfoService.saveBatch(productsInfoEntities);
        }
    }

    @Override
    public void editSupplierInfo(SupplierBaseEntity entity) {
        if (ObjectUtils.isEmpty(entity)){
            throw new RuntimeException("该供应商不存在，请刷新后重试！");
        }
        supplierBaseMapper.updateById(entity);
    }



    @Override
    public SupplierBaseVO getSupplierInfo(String id) {
        if (StringUtils.isEmpty(id)){
            throw new RuntimeException("该供应商不存在，请刷新后重试！");
        }
        SupplierBaseEntity supplierBaseEntity = supplierBaseMapper.selectById(id);
        SupplierBaseVO supplierBaseVO = new SupplierBaseVO();
        BeanUtils.copyProperties(supplierBaseEntity,supplierBaseVO);
        LambdaQueryWrapper<AuthenticationInfoEntity> wrapper1 = Wrappers.lambdaQuery(AuthenticationInfoEntity.class);
        wrapper1.eq(AuthenticationInfoEntity::getSupplierId,id)
                .eq(AuthenticationInfoEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<AuthenticationInfoEntity> authenticationInfoEntities = authenticationInfoMapper.selectList(wrapper1);
        LambdaQueryWrapper<BusinessInfoEntity> wrapper2 = Wrappers.lambdaQuery(BusinessInfoEntity.class);
        wrapper2.eq(BusinessInfoEntity::getSupplierId,id)
                .eq(BusinessInfoEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<BusinessInfoEntity> businessInfoEntities = businessInfoMapper.selectList(wrapper2);
        LambdaQueryWrapper<ProductsInfoEntity> wrapper3 = Wrappers.lambdaQuery(ProductsInfoEntity.class);
        wrapper3.eq(ProductsInfoEntity::getSupplierId,id)
                .eq(ProductsInfoEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<ProductsInfoEntity> productsInfoEntities = productsInfoMapper.selectList(wrapper3);
        LambdaQueryWrapper<ContactsEntity> wrapper4 = Wrappers.lambdaQuery(ContactsEntity.class);
        wrapper4.eq(ContactsEntity::getSupplierId,id)
                .eq(ContactsEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<ContactsEntity> contactsEntities = contactsMapper.selectList(wrapper4);
        supplierBaseVO.setAuthenticationInfo(authenticationInfoEntities);
        supplierBaseVO.setProductsInfo(productsInfoEntities);
        supplierBaseVO.setBusinessInfo(businessInfoEntities);
        supplierBaseVO.setContacts(contactsEntities);
        return supplierBaseVO;
    }

    /**
     * 根据流程实例id获取供应商详情信息
     *
     * @param processInstanceId 流程实例id
     * @return 供应商信息
     */
    @Override
    public SupplierBaseVO getSupplierByProcessInstanceId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)){
            throw new RuntimeException("该流程不存在");
        }
        LambdaQueryWrapper<SupplierBaseEntity> wrapper = Wrappers.lambdaQuery(SupplierBaseEntity.class);
        wrapper.eq(SupplierBaseEntity::getProcessInstanceId,processInstanceId);
        SupplierBaseEntity supplierBaseEntity = supplierBaseMapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(supplierBaseEntity)){
            throw new RuntimeException("该供应商不存在");
        }
        SupplierBaseVO supplierBaseVO = new SupplierBaseVO();
        BeanUtils.copyProperties(supplierBaseEntity,supplierBaseVO);
        LambdaQueryWrapper<AuthenticationInfoEntity> wrapper1 = Wrappers.lambdaQuery(AuthenticationInfoEntity.class);
        String id = supplierBaseEntity.getId();
        wrapper1.eq(AuthenticationInfoEntity::getSupplierId,id)
                .eq(AuthenticationInfoEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<AuthenticationInfoEntity> authenticationInfoEntities = authenticationInfoMapper.selectList(wrapper1);
        LambdaQueryWrapper<BusinessInfoEntity> wrapper2 = Wrappers.lambdaQuery(BusinessInfoEntity.class);
        wrapper2.eq(BusinessInfoEntity::getSupplierId,id)
                .eq(BusinessInfoEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<BusinessInfoEntity> businessInfoEntities = businessInfoMapper.selectList(wrapper2);
        LambdaQueryWrapper<ProductsInfoEntity> wrapper3 = Wrappers.lambdaQuery(ProductsInfoEntity.class);
        wrapper3.eq(ProductsInfoEntity::getSupplierId,id)
                .eq(ProductsInfoEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<ProductsInfoEntity> productsInfoEntities = productsInfoMapper.selectList(wrapper3);
        LambdaQueryWrapper<ContactsEntity> wrapper4 = Wrappers.lambdaQuery(ContactsEntity.class);
        wrapper4.eq(ContactsEntity::getSupplierId,id)
                .eq(ContactsEntity::getDeleteFlag,SupplierBaseConstant.IS_NOT_DELETE);
        List<ContactsEntity> contactsEntities = contactsMapper.selectList(wrapper4);
        supplierBaseVO.setAuthenticationInfo(authenticationInfoEntities);
        supplierBaseVO.setProductsInfo(productsInfoEntities);
        supplierBaseVO.setBusinessInfo(businessInfoEntities);
        supplierBaseVO.setContacts(contactsEntities);
        return supplierBaseVO;
    }
}
