package com.ruoyi.srm.contractMgt.domain.VO;

import com.ruoyi.srm.contractMgt.domain.entity.ContractEntity;
import com.ruoyi.srm.supplierBase.domain.entity.ProductsInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Omelette
 */
@Data
public class ContractRsVO extends ContractEntity {
    private String supplierName;
    private List<ProductsInfoEntity> productList;
}
