package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.workflow.annotaions.ProcessKey;
import lombok.Data;

import java.util.Date;

/**
 * Created by Axel on 2023/6/2 10:22
 *
 * @author Axel
 */
@Data
@TableName("ag_supplier_file_detail")
public class AgFileDetailEntity{
    private static final long serialVersionUID = 1L;

    /** 文件id */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /** 文件访问地址 */
    @Excel(name = "文件访问地址")
    @TableField("url")
    private String url;

    /** 文件大小，单位字节 */
    @Excel(name = "文件大小，单位字节")
    @TableField("size")
    private Long size;

    /** 文件名称 */
    @Excel(name = "文件名称")
    @TableField("filename")
    private String filename;

    /** 原始文件名 */
    @Excel(name = "原始文件名")
    @TableField("original_filename")
    private String originalFilename;

    /** 基础存储路径 */
    @Excel(name = "基础存储路径")
    @TableField("base_path")
    private String basePath;

    /** 存储路径 */
    @Excel(name = "存储路径")
    @TableField("path")
    private String path;

    /** 文件扩展名 */
    @Excel(name = "文件扩展名")
    @TableField("ext")
    private String ext;

    /** MIME类型 */
    @Excel(name = "MIME类型")
    @TableField("content_type")
    private String contentType;

    /** 存储平台 */
    @Excel(name = "存储平台")
    @TableField("platform")
    private String platform;

    /** 缩略图访问路径 */
    @Excel(name = "缩略图访问路径")
    @TableField("th_url")
    private String thUrl;

    /** 缩略图名称 */
    @Excel(name = "缩略图名称")
    @TableField("th_filename")
    private String thFilename;

    /** 缩略图大小，单位字节 */
    @Excel(name = "缩略图大小，单位字节")
    @TableField("th_size")
    private Long thSize;

    /** 缩略图MIME类型 */
    @Excel(name = "缩略图MIME类型")
    @TableField("th_content_type")
    private String thContentType;

    /** 文件所属对象id */
    @Excel(name = "文件所属对象id")
    @TableField("object_id")
    private String objectId;

    /** 文件所属对象类型，例如用户头像，评价图片 */
    @Excel(name = "文件所属对象类型，例如用户头像，评价图片")
    @TableField("object_type")
    private String objectType;

    /** 附加属性 */
    @Excel(name = "附加属性")
    @TableField("attr")
    private String attr;

    /** 创建用户id */
    @Excel(name = "创建用户id")
    @TableField(value = "creator_user_id", fill = FieldFill.INSERT )
    private String creatorUserId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(value = "creator_time", fill = FieldFill.INSERT)
    private Date creatorTime;

    /** 创建用户 */
    @Excel(name = "创建用户")
    @TableField(value = "creator_user", fill = FieldFill.INSERT)
    private String creatorUser;

    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
}
