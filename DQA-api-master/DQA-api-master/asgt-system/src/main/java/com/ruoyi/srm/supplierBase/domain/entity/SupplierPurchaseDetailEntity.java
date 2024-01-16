package com.ruoyi.srm.supplierBase.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author: parus
 * @date: 2023/6/6 10:03
 */
@Data
@TableName(value = "ag_supplier_purchase_detail")
public class SupplierPurchaseDetailEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId
    private String id;

    /** 采购物品名称 */
    @Excel(name = "采购物品名称")
    private String itemName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specificationModel;

    /** 数量 */
    @Excel(name = "数量")
    private Integer num;

    /** 单价 */
    @Excel(name = "单价")
    private Integer price;

    /** 参考供应商 */
    @Excel(name = "参考供应商")
    private String referenceSupplier;

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
    private String lastModifyUser;

    /** 获批供应商需求申请id */
    @Excel(name = "获批供应商需求申请id")
    private String approvedRequirementId;

    /** 是否删除（0未删 1已删） */
    @Excel(name = "是否删除", readConverterExp = "0=未删,1=已删")
    private Integer deleteFlag;
}
