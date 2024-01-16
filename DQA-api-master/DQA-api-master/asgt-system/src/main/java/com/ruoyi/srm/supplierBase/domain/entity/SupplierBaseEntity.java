package com.ruoyi.srm.supplierBase.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author: dyw
 * @date: 2023/5/24 9:00
 */
@Data
@TableName(value = "ag_supplier_base")
public class SupplierBaseEntity  {
        @TableField(exist = false)
        private static final long serialVersionUID = 1L;

        /** 主键id */
        @TableId("id")
        private String id;

        /** 入册审核单位id */
        @Excel(name = "入册审核单位id")
        private String companyId;

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

        /** 编号 */
        @Excel(name = "编号")
        private String code;

        /** 等级（0未定 1一级 2二级 3三级） */
        @Excel(name = "等级", readConverterExp = "0=未定,1=一级,2=二级,3=三级")
        private Integer level;

        /** 合格评定（0不合格 1合格 2未评） */
        @Excel(name = "合格评定", readConverterExp = "0=不合格,1=合格,2=未评")
        private Integer isQualified;

        /** 合格有效期 */
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Excel(name = "合格有效期", width = 30, dateFormat = "yyyy-MM-dd")
        private Date qualifiedDate;

        /** 状态 */
        @Excel(name = "状态")
        private String status;

        /** 会计年度开始日期 */
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Excel(name = "会计年度开始日期", width = 30, dateFormat = "yyyy-MM-dd")
        private Date fiscalStartDate;

        /** 银行综合资信证明 */
        @Excel(name = "银行综合资信证明")
        private String creditCertificateFile;

        /** 上三个会计年度经审计的收入额（万元） */
        @Excel(name = "上三个会计年度经审计的收入额", readConverterExp = "万=元")
        private Integer lastQuarterEarn;

        /** 审核报告 */
        @Excel(name = "审核报告")
        private Integer auditReportsFile;

        /** 分类（1货源 2潜在 3获批） */
        @Excel(name = "分类", readConverterExp = "1=货源,2=潜在,3=获批")
        private Integer category;

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

        /** 是否删除（0未删 1已删） */
        @Excel(name = "是否删除", readConverterExp = "0=未删,1=已删")
        private Integer deleteFlag;

        /** 货源入册时间 */
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Excel(name = "货源入册时间", width = 30, dateFormat = "yyyy-MM-dd")
        private Date sourceTime;

        /** 潜在入册时间 */
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Excel(name = "潜在入册时间", width = 30, dateFormat = "yyyy-MM-dd")
        private Date potentialTime;

        /** 获批入册时间 */
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Excel(name = "获批入册时间", width = 30, dateFormat = "yyyy-MM-dd")
        private Date approveTime;

        /** 流程实例id */
        @Excel(name = "流程实例id")
        private String processInstanceId;

        @Excel(name = "是否入册")
        private Integer ableFlag;
}
