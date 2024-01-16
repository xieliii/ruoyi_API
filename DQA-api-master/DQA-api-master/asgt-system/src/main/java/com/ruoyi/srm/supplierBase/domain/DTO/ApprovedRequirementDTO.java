package com.ruoyi.srm.supplierBase.domain.DTO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierListEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierPurchaseDetailEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: parus
 * @date: 2023/6/6 10:07
 */
@Data
public class ApprovedRequirementDTO {
    @TableId
    private String id;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String projectCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String projectName;

    /** 需求编号 */
    @Excel(name = "需求编号")
    private String reqCode;

    /** 需求交付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "需求交付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reqDeliveryTime;

    /** 供应商类别（1一类 2二类 3三类 4四类包含设计 5四类不包含设计 6五类包含设计 7五类不包含设计） */
    @Excel(name = "供应商类别", readConverterExp = "1=一类,2=二类,3=三类,4=四类包含设计,5=四类不包含设计,6=五类包含设计,7=五类不包含设计")
    private Integer category;

    /** 附件id */
    @Excel(name = "附件id")
    private String fileId;

    /** 供应商部门主管id */
    @Excel(name = "供应商部门主管id")
    private String managerId;

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

    /**  */
    @Excel(name = "")
    private String rfiId;

    /**  */
    @Excel(name = "")
    private String rfpId;

    /**  */
    @Excel(name = "")
    private String rfqId;

    /** rfi评估报告文件id */
    @Excel(name = "rfi评估报告文件id")
    private String rfiEstimateReport;

    /** rfp评估报告文件id */
    @Excel(name = "rfp评估报告文件id")
    private String rfpEstimateReport;

    /** rfq评估报告文件id */
    @Excel(name = "rfq评估报告文件id")
    private String rfqEstimateReport;

    /** 单一货源说明文件id */
    @Excel(name = "单一货源说明文件id")
    private String singleReport;

    /** 是否为单一货源 0否1是 */
    @Excel(name = "是否为单一货源 0否1是")
    private Integer singleFlag;

    /** 是否需要rfp 0否1是 */
    @Excel(name = "是否需要rfp 0否1是")
    private Integer needRfpFlag;

    /** 流程实例id */
    @Excel(name = "流程实例id")
    private String processInstanceId;

    /** rfi流程实例id */
    @Excel(name = "rfi流程实例id")
    private String rfiProcessInstanceId;

    /** rfp流程实例id */
    @Excel(name = "rfp流程实例id")
    private String rfpProcessInstanceId;

    /** rfq流程实例id */
    @Excel(name = "rfq流程实例id")
    private String rfqProcessInstanceId;

    private String searchId;
    private String processKey;

    private List<SupplierPurchaseDetailEntity> purchaseDetailList;

    private List<SupplierListEntity> supplierList;
}
