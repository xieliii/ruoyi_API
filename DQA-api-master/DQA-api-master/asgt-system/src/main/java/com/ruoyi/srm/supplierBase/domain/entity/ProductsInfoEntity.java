package com.ruoyi.srm.supplierBase.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: dyw
 * @date: 2023/5/24 10:02
 */
@Data
@TableName("ag_supplier_products")
public class ProductsInfoEntity  {
    private static final long serialVersionUID = 1L;

    /** id 主键 雪花 */
    @TableId
    private String id;

    /** 关联供应商id */
    @Excel(name = "关联供应商id")
    private String supplierId;

    /** 产品/服务名称 */
    @Excel(name = "产品/服务名称")
    private String name;

    /** product 产品 service 服务 */
    @Excel(name = "product 产品 service 服务")
    private Integer kindCode;

    /** 产品唯一 */
    @Excel(name = "产品唯一")
    private String productUnique;

    /** 产品型号 */
    @Excel(name = "产品型号")
    private String productModel;

    /** 供应商类别 123 为123类 45为4/5类包含设计 67为45类不包含设计 */
    @Excel(name = "供应商类别 123 为123类 45为4/5类包含设计 67为45类不包含设计")
    private Integer supplierType;

    /** 取证情况 为文件 */
    @Excel(name = "取证情况 为文件")
    private String fileId;

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
}
