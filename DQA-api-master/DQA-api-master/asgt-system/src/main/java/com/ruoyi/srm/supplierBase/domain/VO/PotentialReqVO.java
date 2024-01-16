package com.ruoyi.srm.supplierBase.domain.VO;
import com.ruoyi.srm.supplierBase.domain.entity.PotentialRequirementEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import lombok.Data;
import java.util.List;

/**
 * @author Omelette
 */
@Data
public class PotentialReqVO extends PotentialRequirementEntity{
    private List<SupplierBaseEntity> supplierList;
}
