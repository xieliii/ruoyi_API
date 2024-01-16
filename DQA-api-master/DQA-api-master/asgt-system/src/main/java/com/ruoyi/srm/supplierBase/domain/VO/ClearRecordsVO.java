package com.ruoyi.srm.supplierBase.domain.VO;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.srm.supplierBase.domain.entity.ClearRecordsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierClearRsEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author: dyw
 * @date: 2023/6/1 16:29
 */
@Data
public class ClearRecordsVO extends ClearRecordsEntity {

    /** 是否删除（0未删 1已删） */
    @Excel(name = "是否删除", readConverterExp = "0=未删,1=已删")
    private Integer deleteFlag;

    /** 是否删除（0未删 1已删） */
    @Excel(name = "文件id")
    private String fileId;

    private List<SupplierClearRsVO> supplierList;

    /**
     * 用，隔开的发起清退的供应商名称
     */
    private String supplierNames;
}
