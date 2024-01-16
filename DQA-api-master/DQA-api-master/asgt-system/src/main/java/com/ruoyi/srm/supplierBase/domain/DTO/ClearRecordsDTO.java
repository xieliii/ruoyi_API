package com.ruoyi.srm.supplierBase.domain.DTO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierClearRsEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: dyw
 * @date: 2023/6/2 16:12
 */
@Data
public class ClearRecordsDTO {
    /** 主键 雪花 */
    @TableId
    private String id;

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
    @Excel(name = "文件id")
    private String fileId;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String processInstanceId;

    private List<SupplierClearRsEntity> supplierIdList;
}
