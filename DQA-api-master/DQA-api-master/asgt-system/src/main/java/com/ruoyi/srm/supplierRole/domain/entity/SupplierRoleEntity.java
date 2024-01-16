package com.ruoyi.srm.supplierRole.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * @author: dyw
 * @date: 2023/5/24 11:37
 */
@Data
@TableName("ag_supplier_role")
public class SupplierRoleEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键id */
    @TableId
    private String id;

    /** 角色代号 */
    @Excel(name = "角色代号")
    private String roleCode;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 描述 */
    @Excel(name = "描述")
    private Long description;

    /** 是否启用 */
    @Excel(name = "是否启用")
    private Long isEnabled;
}
