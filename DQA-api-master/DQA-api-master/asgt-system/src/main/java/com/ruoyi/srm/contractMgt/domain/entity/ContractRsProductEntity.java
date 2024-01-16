package com.ruoyi.srm.contractMgt.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * @author Omelette
 */
@Data
@TableName("ag_supplier_contract_rs_product")
public class ContractRsProductEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    @Excel(name = "合同id")
    private String contractId;

    @Excel(name = "产品id")
    private String productId;

    @Excel(name = "供应商id")
    private String supplierId;
}
