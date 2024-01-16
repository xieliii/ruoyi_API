package com.ruoyi.test.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.test.domain.Test;
import com.ruoyi.test.service.ITestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2022-12-14
 */
@Api(tags = {"测试接口"})
@RestController
@RequestMapping("/system/test")
public class Test2Controller extends BaseController
{
    @Autowired
    private ITestService testService;

    /**
     * 查询【请填写功能名称】列表
     */
    @ApiOperation("list")
    @GetMapping("/list")
    public TableDataInfo list(Test test)
    {
        startPage();
        List<Test> list = testService.selectTestList(test);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @ApiOperation("export")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Test test)
    {
        List<Test> list = testService.selectTestList(test);
        ExcelUtil<Test> util = new ExcelUtil<Test>(Test.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @ApiOperation("getInfo")
    @GetMapping(value = "/{id}")
    public R<Test> getInfo(@PathVariable("id") String id)
    {
        return R.ok(testService.selectTestById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @ApiOperation("add")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody Test test)
    {
        return toResponse(testService.insertTest(test));
    }

    /**
     * 修改【请填写功能名称】
     */
    @ApiOperation("edit")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody Test test)
    {
        return toResponse(testService.updateTest(test));
    }

    /**
     * 删除【请填写功能名称】
     */
    @ApiOperation("remove")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R remove(@PathVariable String[] ids)
    {
        return toResponse(testService.deleteTestByIds(ids));
    }
}
