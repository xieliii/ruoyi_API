package com.ruoyi.srm.supplierBase.domain.VO;

import com.ruoyi.srm.supplierBase.domain.entity.ApprovedRequirementEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierListEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierPurchaseDetailEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Omelette
 */
@Data
public class ApprovedReqVO extends ApprovedRequirementEntity {
    private List<SupplierPurchaseDetailEntity> purchaseList;
    private List<SupplierBaseEntity> supplierList;
}
