package com.ruoyi.srm.supplierBase.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.srm.supplierBase.domain.DTO.ClearRecordsDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchClearRecordsDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ClearRecordsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ClearRecordsEntity;
import com.ruoyi.srm.supplierBase.service.ClearRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;

/**
 * @author: dyw
 * @date: 2023/6/1 13:59
 */
@Api(tags = "供应商清退基础接口")
@RestController
@RequestMapping("/supplier-clear")
public class ClearRecordsController extends BaseController {

    @Resource
    private ClearRecordsService clearRecordsService;


    @ApiOperation(value = "查询清退记录")
    @PostMapping("/getClearList")
    public TableDataInfo getClearList(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestBody SearchClearRecordsDTO dto) {
        startPage();
        List<ClearRecordsVO> categorySumVO = clearRecordsService.getClearList(dto);
        return getDataTable(categorySumVO);
    }

    @ApiOperation(value = "查询清退记录")
    @PostMapping("/getClearByProcessInstanceId")
    public R<ClearRecordsVO> getClearByProcessInstanceId(@RequestParam String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)){
            R.fail();
        }
        ClearRecordsVO clearRecordsVO = clearRecordsService.getClearByProcessInstanceId(processInstanceId);
        return R.ok(clearRecordsVO);
    }

    @ApiOperation(value = "查询清退记录")
    @PostMapping("/getClearById")
    public R<ClearRecordsVO> getClearById(@RequestParam String id) {
        ClearRecordsVO clearRecordsVO = clearRecordsService.getClearById(id);
        return R.ok(clearRecordsVO);
    }

    @ApiOperation(value = "增加清退记录")
    @PostMapping("/addClearRecords")
    public R<String> addClearRecords(@RequestBody ClearRecordsDTO dto) {
        clearRecordsService.addClearRecords(dto);
        return R.ok();
    }
}
