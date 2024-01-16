package com.ruoyi.srm.supplierBase.domain.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: dyw
 * @date: 2023/6/2 11:02
 */
@Data
public class SearchChangeLogDTO {
    private String supplierName;
    private Integer companyNature;
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    private Date endDate;
}
