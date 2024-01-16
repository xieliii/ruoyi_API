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
 * @date: 2023/5/24 10:59
 */
@Data
@TableName("ag_supplier_potential_requirement")
public class PotentialRequirementEntity  {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId
    private String id;

    /** 需求编号 */
    @Excel(name = "需求编号")
    private String reqCode;

    /** 供应商部门主管id */
    @Excel(name = "供应商部门主管id")
    private String managerId;

    /** 需求名称 */
    @Excel(name = "需求名称")
    private String reqName;

    /** 供应商类别 */
    @Excel(name = "供应商类别")
    private String category;

    /** 需求来源说明 */
    @Excel(name = "需求来源说明")
    private String sourceDescription;

    /** 需求论证材料附件 */
    @Excel(name = "需求论证材料附件")
    private String argumentationFileId;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String processInstanceId;

    /** 创建用户id */
    @Excel(name = "创建用户id")
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT)
    private String creatorUserId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @Excel(name = "创建用户")
    @TableField(value = "creator_user", fill = FieldFill.INSERT)
    private String creatorUser;

    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
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

    @Excel(name = "信息调查附件表")
    private String infoSurvey;
}
