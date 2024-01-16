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
 * @date: 2023/5/24 10:05
 */
@Data
@TableName("ag_supplier_authentication_info")
public class AuthenticationInfoEntity  {
    private static final long serialVersionUID = 1L;

    /** 主键 雪花 */
    @TableId
    private String id;

    /** 关联供应商id */
    @Excel(name = "关联供应商id")
    private String supplierId;

    /** 认证标准 */
    @Excel(name = "认证标准")
    private String standard;

    /** 证书编号 */
    @Excel(name = "证书编号")
    private String certificateNo;

    /** 认证范围 */
    @Excel(name = "认证范围")
    private String certificationScope;

    /** 有效期开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date validityStartTime;

    /** 有效期结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date validityEndTime;

    /** 认证资料 关联文件id */
    @Excel(name = "认证资料 关联文件id")
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
