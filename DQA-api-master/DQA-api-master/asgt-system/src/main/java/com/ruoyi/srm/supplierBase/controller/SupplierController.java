package com.ruoyi.srm.supplierBase.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.srm.supplierBase.constant.SupplierBaseConstant;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchChangeLogDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.SearchSuppliersDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.SupplierBaseDTO;
import com.ruoyi.srm.supplierBase.domain.VO.CategorySumVO;
import com.ruoyi.srm.supplierBase.domain.VO.ChangeRecordsVO;
import com.ruoyi.srm.supplierBase.domain.VO.SupplierBaseVO;
import com.ruoyi.srm.supplierBase.domain.entity.ChangeRecordsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.service.ChangeLogService;
import com.ruoyi.srm.supplierBase.service.SupplierBaseService;
import com.ruoyi.srm.supplierBase.utils.SupplierUtil;
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
 * @date: 2023/5/24 8:56
 */
@Api(tags = "供应商基础接口")
@RestController
@RequestMapping("/supplier")
public class SupplierController extends BaseController {

    @Resource
    private SupplierBaseService supplierBaseService;

    @Resource
    private ChangeLogService changeLogService;

    @ApiOperation(value = "查询各类供应商数目")
    @PostMapping("/getCategorySum")
    public R<CategorySumVO> getCategorySum() {
        CategorySumVO categorySumVO = supplierBaseService.getCategorySum();
        return R.ok(categorySumVO);
    }

    @ApiOperation(value = "编辑供应商信息 正式版要发起流程 目前仅做测试用接口")
    @PostMapping("/editSupplierInfo")
    public R<String> editSupplierInfo(@RequestBody SupplierBaseEntity entity) {
        supplierBaseService.editSupplierInfo(entity);
        return R.ok();
    }

    @ApiOperation(value = "按照分类分页查询供应商列表")
    @PostMapping("/listAllSupplierByPage")
    public TableDataInfo listAllSupplierByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestBody SearchSuppliersDTO dto) {
        startPage();
        List<SupplierBaseEntity> supplier = supplierBaseService.listAllSupplierByPage(dto);
        return getDataTable(supplier);
    }

    @ApiOperation(value = "按照分类分页查询供应商列表")
    @PostMapping("/listAllSupplierByKind")
    public R<List<SupplierBaseEntity>> listAllSupplierByKind(@RequestParam Integer kind) {
        List<SupplierBaseEntity> supplier = supplierBaseService.listAllSupplierByKind(kind);
        return R.ok(supplier);
    }

    @ApiOperation(value = "添加供应商 正式版要发起流程 目前仅做测试用接口")
    @PostMapping("/addSupplier")
    public R<String> addSupplier(@RequestBody SupplierBaseDTO baseDTO) {
        supplierBaseService.addSupplier(baseDTO);
        return R.ok();
    }

    @ApiOperation(value = "供应商详情")
    @PostMapping("/getSupplierInfo")
    public R<SupplierBaseVO> getSupplierInfo(@RequestParam String id) {
        return R.ok(supplierBaseService.getSupplierInfo(id));
    }

    @ApiOperation(value = "供应商详情")
    @PostMapping("/getSupplierByProcessInstanceId")
    public R<SupplierBaseVO> getSupplierByProcessInstanceId(@RequestParam String processInstanceId) {
        return R.ok(supplierBaseService.getSupplierByProcessInstanceId(processInstanceId));
    }

    @ApiOperation(value = "更新供应商记录表单添加")
    @PostMapping("/addChangeLog")
    public R<String> addChangeLog(@RequestBody ChangeRecordsEntity changeRecordsEntity) {
        changeLogService.addChangeLog(changeRecordsEntity);
        return R.ok();
    }

    @ApiOperation(value = "获取更新记录")
    @PostMapping("/getChangeLog")
    public R<ChangeRecordsVO> getChangeLog(@RequestParam String id) {
        return R.ok(changeLogService.getChangeLog(id));
    }

    @ApiOperation(value = "根据流程实例id获取更新记录")
    @PostMapping("/getChangeByProcessInstanceId")
    public R<ChangeRecordsEntity> getChangeLogByProcessInstanceId(@RequestParam String processInstanceId) {
        return R.ok(changeLogService.getChangeByProcessInstanceId(processInstanceId));
    }

    @ApiOperation(value = "获取分页更新记录")
    @PostMapping("/getChangeLogByPage")
    public TableDataInfo getChangeLogByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestBody SearchChangeLogDTO dto) {
        startPage();
        List<ChangeRecordsVO> recordsVOS = changeLogService.getChangeLogByPage(dto);
        return getDataTable(recordsVOS);
    }

}
