package com.ruoyi.srm.supplierBase.domain.DTO;

import lombok.Data;

/**
 * @author: dyw
 * @date: 2023/5/29 9:43
 */
@Data
public class SearchSuppliersDTO {
    private Integer kind;
    private Integer companyNature;
    private String code;
    private String supplierName;
    private Integer category;
}
