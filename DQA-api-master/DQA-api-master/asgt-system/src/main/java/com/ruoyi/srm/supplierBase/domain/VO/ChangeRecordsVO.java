package com.ruoyi.srm.supplierBase.domain.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author: dyw
 * @date: 2023/5/25 15:51
 */
@Data
public class ChangeRecordsVO {
    /** 主键id */
    private String id;

    /** 供应商名称 */
    @Excel(name = "供应商名称")
    private String supplierName;

    /** 控股公司名称 */
    @Excel(name = "控股公司名称")
    private String holdingCompany;

    /** 商业注册号 */
    @Excel(name = "商业注册号")
    private String businessNum;

    /** 成立时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成立时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date establishDate;

    /** 法人姓名 */
    @Excel(name = "法人姓名")
    private String juristicPersonName;

    /** 注册资本（万元） */
    @Excel(name = "注册资本", readConverterExp = "万=元")
    private Integer registeredCapital;

    /** 公司性质（1国有企业 2私营企业 3外资企业 4境外企业 5内部供应商） */
    @Excel(name = "公司性质", readConverterExp = "1=国有企业,2=私营企业,3=外资企业,4=境外企业,5=内部供应商")
    private Integer companyNature;

    /** 注册地址 */
    @Excel(name = "注册地址")
    private String registeredAddress;

    /** 竞争优势 */
    @Excel(name = "竞争优势")
    private String advantage;

    /** 业务描述 */
    @Excel(name = "业务描述")
    private String businessDescription;

    /** 会计年度开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会计年度开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date fiscalStartDate;

    /** 银行综合资信证明 */
    @Excel(name = "银行综合资信证明")
    private Integer creditCertificateFile;

    /** 上三个会计年度经审计的收入额（万元） */
    @Excel(name = "上三个会计年度经审计的收入额", readConverterExp = "万=元")
    private Integer lastQuarterEarn;

    /** 创建用户id */
    @Excel(name = "创建用户id")
    private String creatorUserId;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date creatorTime;

    /** 创建用户 */
    @Excel(name = "创建用户")
    private String creatorUser;

    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastModifyTime;

    /** 更新人id */
    @Excel(name = "更新人id")
    private String lastModifyUserId;

    /** 更新用户信息 */
    @Excel(name = "更新用户信息")
    private String lastModifyUser;

    /** 是否删除（0未删 1已删） */
    @Excel(name = "是否删除", readConverterExp = "0=未删,1=已删")
    private Integer deleteFlag;

    /** 申请变更的供应商id */
    @Excel(name = "申请变更的供应商id")
    private String supplierId;

    /** 变更记录是否通过 */
    @Excel(name = "变更记录是否通过")
    private Integer approveFlag;

    /** 入册申请单位id */
    @Excel(name = "入册申请单位id")
    private String companyId;
    private String companyName;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String processInstanceId;
}
