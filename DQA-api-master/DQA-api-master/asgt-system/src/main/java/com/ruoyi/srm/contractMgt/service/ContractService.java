package com.ruoyi.srm.contractMgt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.srm.contractMgt.domain.DTO.ContractDTO;
import com.ruoyi.srm.contractMgt.domain.VO.ContractRsVO;
import com.ruoyi.srm.contractMgt.domain.entity.ContractEntity;

import java.util.List;

/**
 * @author Omelette
 */
public interface ContractService extends IService<ContractEntity> {
    /**
     * 分页初始化
     * @param dto 查询数据
     * @return 分页数据
     */
    IPage<ContractRsVO> getContractByPage(ContractDTO dto);

    /**
     * 根据id查询
     * @param id 合同id
     * @return 合同信息
     */
    ContractRsVO getContractById(String id);

    /**
     * 新增
     * @param dto 合同实体和产品列表
     * @return 结果
     */
    Boolean addContract(ContractDTO dto);

    /**
     * 更新/修改
     * @param dto 合同信息
     * @return 结果
     */
    Integer updateContract(ContractDTO dto);

    /**
     * 删除
     * @param id 合同id
     * @return 结果
     */
    Integer delContract(String id);

    /**
     * 批量删除
     * @param ids id列表
     * @return 结果
     */
    Integer batchDelContract(List<String> ids);


}
