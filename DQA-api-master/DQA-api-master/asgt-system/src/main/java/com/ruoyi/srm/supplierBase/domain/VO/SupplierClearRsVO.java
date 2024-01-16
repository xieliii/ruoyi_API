package com.ruoyi.srm.supplierBase.domain.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @author: dyw
 * @date: 2023/6/2 16:18
 */
@Data
public class SupplierClearRsVO {
    /** 雪花 主键 */
    private String id;

    /** 关联的清退表单id */
    @Excel(name = "关联的清退表单id")
    private String clearRecordId;

    /** 清退表单中的供应商id */
    @Excel(name = "清退表单中的供应商id")
    private String supplierId;
    private String supplierName;

    /** 是否审批通过 0否1是 */
    @Excel(name = "是否审批通过 0否1是")
    private Integer approvedFlag;

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

    /** 是否删除（0未删 1已删） */
    @Excel(name = "是否删除", readConverterExp = "0=未删,1=已删")
    private Integer deleteFlag;

    /** 清退说明 */
    @Excel(name = "清退说明")
    private String description;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String processInstanceId;
}
