package com.ruoyi.srm.supplierBase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchSuppliersDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.SupplierBaseDTO;
import com.ruoyi.srm.supplierBase.domain.VO.CategorySumVO;
import com.ruoyi.srm.supplierBase.domain.VO.SupplierBaseVO;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;

import java.util.List;

/**
 * 供应商基础信息service
 * @author: dyw
 * @date: 2023/5/24 11:43
 */
public interface SupplierBaseService extends IService<SupplierBaseEntity> {

    /**
     * 获取各类供应商数量
     * @return
     */
    CategorySumVO getCategorySum();

    /**
     * 按照类别分页列出供应商清册
     * @param dto 输入
     * @return 供应商清册
     */
    List<SupplierBaseEntity>  listAllSupplierByPage(SearchSuppliersDTO dto);

    /**
     * 按照分类列出所有供应商清册 用作选择器 当kind 为null 的时候筛选全部
     * @param kind
     * @return
     */
    List<SupplierBaseEntity> listAllSupplierByKind(Integer kind);

    /**
     * 添加供应商
     * @param supplierBaseDTO
     */
    void addSupplier(SupplierBaseDTO supplierBaseDTO);

    /**
     * 更新供应商信息
     * @param entity 更改信息实体类
     */
    void  editSupplierInfo(SupplierBaseEntity entity);



    /**
     * 获取供应商详情信息
     * @param id 供应商id
     * @return 供应商信息
     */
    SupplierBaseVO getSupplierInfo(String id);


    /**
     * 根据流程实例id获取供应商详情信息
     * @param processInstanceId 流程实例id
     * @return 供应商信息
     */
    SupplierBaseVO getSupplierByProcessInstanceId(String processInstanceId);
}
