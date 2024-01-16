package com.ruoyi.srm.supplierBase.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author: dyw
 * @date: 2023/6/5 15:58
 */
@Data
@TableName(value = "ag_supplier_list")
public class SupplierListEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String id;

    /** 供应商id */
    @Excel(name = "供应商id")
    private String supplierId;

    /** 供应商编号 */
    @Excel(name = "供应商编号")
    private String supplierCode;

    /** 公司性质（1国有企业 2私营企业 3外资企业 4境外企业 5内部供应商） */
    @Excel(name = "公司性质", readConverterExp = "1=国有企业,2=私营企业,3=外资企业,4=境外企业,5=内部供应商")
    private Integer companyNature;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 创建用户id */
    @Excel(name = "创建用户id")
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT)
    private String creatorUserId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @Excel(name = "创建用户")
    @TableField(value = "creator_user", fill = FieldFill.INSERT)
    private String creatorUser;

    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "last_modify_time", fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /** 更新人id */
    @Excel(name = "更新人id")
    @TableField(value = "last_modify_user_id", fill = FieldFill.UPDATE)
    private String lastModifyUserId;

    /** 更新用户信息 */
    @Excel(name = "更新用户信息")
    @TableField(value = "last_modify_user", fill = FieldFill.UPDATE)
    private String lastModifyUser;

    /** 获批供应商需求申请id */
    @Excel(name = "获批供应商需求申请id")
    private String approvedRequirementId;

    /** 潜在供应商需求申请id */
    @Excel(name = "潜在供应商需求申请id")
    private String potentialReqId;

    /** 是否删除（0未删 1已删） */
    @Excel(name = "是否删除", readConverterExp = "0=未删,1=已删")
    private Integer deleteFlag;

    /** 回复的rfi文件 */
    @Excel(name = "回复的rfi文件")
    private String rfiId;

    /** 回复的RFP文件 */
    @Excel(name = "回复的RFP文件")
    private String rfpId;

    /** 回复的RFQ文件 */
    @Excel(name = "回复的RFQ文件")
    private String rfqId;

    /** 是否通过审批 0否1是 */
    @Excel(name = "是否通过审批 0否1是")
    private Integer approvedFlag;
}
