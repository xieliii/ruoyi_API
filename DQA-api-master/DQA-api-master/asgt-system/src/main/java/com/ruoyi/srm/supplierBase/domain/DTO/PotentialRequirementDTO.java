package com.ruoyi.srm.supplierBase.domain.DTO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierListEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: parus
 * @date: 2023/6/6 10:37
 */
@Data
public class PotentialRequirementDTO {
    private String id;
    private String reqCode;
    private String managerId;
    private String reqName;
    private String category;
    private String sourceDescription;
    private String argumentationFileId;
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT)
    private String creatorUserId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;
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

    private Integer deleteFlag;

    private String processInstanceId;

    private List<SupplierListEntity> supplierList;
}
