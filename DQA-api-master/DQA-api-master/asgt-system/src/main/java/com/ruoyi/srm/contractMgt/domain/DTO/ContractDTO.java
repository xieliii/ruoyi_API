package com.ruoyi.srm.contractMgt.domain.DTO;

import com.ruoyi.srm.contractMgt.domain.entity.ContractEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Omelette
 */
@Data
public class ContractDTO extends ContractEntity {
    private Integer pageNum;
    private Integer pageSize;
    private List<String> productIds;
}
