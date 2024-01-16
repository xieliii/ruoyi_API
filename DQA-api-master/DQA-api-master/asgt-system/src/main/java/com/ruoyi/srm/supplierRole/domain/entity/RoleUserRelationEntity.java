package com.ruoyi.srm.supplierRole.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * @author: dyw
 * @date: 2023/5/24 11:37
 */
@Data
@TableName("ag_supplier_rs_role_user")
public class RoleUserRelationEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 自然主键 雪花算法 */
    @TableId
    private String id;

    /** ecm角色表 角色code */
    @Excel(name = "ecm角色表 角色code")
    private Long roleCode;

    /** 用户id */
    @Excel(name = "用户id")
    private String userId;

    /** 用户ecm角色说明 */
    @Excel(name = "用户ecm角色说明")
    private String description;

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
}
