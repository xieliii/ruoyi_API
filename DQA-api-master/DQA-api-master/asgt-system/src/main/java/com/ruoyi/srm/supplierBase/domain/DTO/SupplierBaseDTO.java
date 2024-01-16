package com.ruoyi.srm.supplierBase.domain.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.srm.supplierBase.domain.entity.AuthenticationInfoEntity;
import com.ruoyi.srm.supplierBase.domain.entity.BusinessInfoEntity;
import com.ruoyi.srm.supplierBase.domain.entity.ContactsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.ProductsInfoEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: dyw
 * @date: 2023/5/26 10:08
 */
@Data
public class SupplierBaseDTO {
    private String companyId;
    private String supplierName;
    private String holdingCompany;
    private String businessNum;
    private Date establishDate;
    private String juristicPersonName;
    private Integer registeredCapital;
    private Integer companyNature;
    private String registeredAddress;
    private String advantage;
    private String businessDescription;
    private String code;
    private Integer level;
    private Integer isQualified;
    private Date qualifiedDate;
    private String status;
    private Date fiscalStartDate;
    private String creditCertificateFile;
    private Integer lastQuarterEarn;
    private Integer auditReportsFile;
    private Integer category;
    private String creatorUserId;
    private Date creatorTime;
    private String creatorUser;
    private Date lastModifyTime;
    private String lastModifyUserId;
    private String lastModifyUser;
    private Integer deleteFlag;
    private Date sourceTime;
    private Date potentialTime;
    private Date approveTime;
    private String processInstanceId;
    private Integer ableFlag;
    private List<AuthenticationInfoEntity> authenticationInfoEntities;
    private List<ProductsInfoEntity> productsInfoEntities;
    private List<ContactsEntity> contactsEntities;
    private List<BusinessInfoEntity> businessInfoEntities;
}
