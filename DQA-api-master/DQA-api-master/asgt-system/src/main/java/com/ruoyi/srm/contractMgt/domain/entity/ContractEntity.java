package com.ruoyi.srm.contractMgt.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author Omelette
 */
@Data
@TableName("ag_supplier_contract")
public class ContractEntity {
    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    @Excel(name = "合同名称")
    private String name;

    @Excel(name = "合同编号")
    private String code;

    @Excel(name = "合同状态（履约中、已到期）")
    private Integer status;

    @Excel(name = "合同类型")
    private Integer contractType;

    @Excel(name = "合同主要内容")
    private String content;

    @Excel(name="合同开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date startDate;

    @Excel(name="合同结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date endDate;

    @Excel(name = "合同文件")
    private String contractFile;

    @Excel(name = "乙方负责人")
    private String supplierPrincipal;

    @Excel(name = "联系电话")
    private String telephone;

    @Excel(name = "项目名称")
    private String projectName;

    @Excel(name = "项目描述")
    private String projectDescription;

    @Excel(name = "项目负责人")
    private String projectPrincipal;

    @Excel(name = "需求单位")
    private String companyId;

    @Excel(name = "供应商id")
    private String supplierId;

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
    @TableField(value = "last_modify_user_id", fill = FieldFill.UPDATE)
    private Date lastModifyTime;

    /** 更新人id */
    @Excel(name = "更新人id")
    @TableField(value = "last_modify_time", fill = FieldFill.UPDATE)
    private String lastModifyUserId;

    /** 更新用户信息 */
    @Excel(name = "更新用户信息")
    @TableField(value = "last_modify_user", fill = FieldFill.UPDATE)
    private String lastModifyUser;

    @Excel(name = "是否删除", readConverterExp = "0=未删,1=已删")
    private Integer deleteFlag;
}
