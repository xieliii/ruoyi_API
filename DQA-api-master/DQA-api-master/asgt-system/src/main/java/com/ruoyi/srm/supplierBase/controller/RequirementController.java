package com.ruoyi.srm.supplierBase.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.srm.supplierBase.domain.DTO.ApprovedRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.PotentialRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.VO.PotentialReqVO;
import com.ruoyi.srm.supplierBase.domain.VO.WorkflowRsApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.ApprovedRequirementEntity;
import com.ruoyi.srm.supplierBase.service.ApprovedRequirementService;
import com.ruoyi.srm.supplierBase.service.PotentialRequirementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: parus
 * @date: 2023/6/5 16:10
 */
@Api(value = "需求接口")
@RestController
@RequestMapping("/supplier-requirement")
public class RequirementController {

    @Resource
    private ApprovedRequirementService approvedRequirementService;
    @Resource
    private PotentialRequirementService potentialRequirementService;

    @ApiOperation(value = "查询需求记录 分页")
    @PostMapping("/getApprovedReqByPage")
    public R<IPage<ApprovedRequirementEntity>> getApprovedReqByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        approvedRequirementService.getApprovedReqByPage(pageNum,pageSize);
        return R.ok();
    }

    @ApiOperation(value = "查询需求记录 分页")
    @PostMapping("/getPotentialReqByPage")
    public R<IPage<ApprovedRequirementEntity>> getPotentialReqByPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        potentialRequirementService.getPotentialReqByPage(pageNum,pageSize);
        return R.ok();
    }

    @ApiOperation(value = "根据流程实例id潜在入册需求信息")
    @PostMapping("/getPotentialByProcessInstanceId")
    public R<PotentialReqVO> getPotentialByProcessInstanceId(@RequestBody PotentialRequirementDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            R.fail();
        }
        String processInstanceId = dto.getProcessInstanceId();
        if (StringUtils.isEmpty(processInstanceId)){
            R.fail("流程实例id为空");
        }
        return R.ok(potentialRequirementService.getPotentialByProcessInstanceId(processInstanceId));
    }

    @ApiOperation(value = "根据流程实例id潜在入册需求信息")
    @PostMapping("/getApprovedByProcessInstanceId")
    public R<WorkflowRsApprovedReqVO> getApprovedByProcessInstanceId(@RequestBody ApprovedRequirementDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            R.fail();
        }
        String processInstanceId = dto.getProcessInstanceId();
        if (StringUtils.isEmpty(processInstanceId)){
            R.fail("流程实例id为空");
        }
        return R.ok(approvedRequirementService.getApprovedByProcessInstanceId(processInstanceId));
    }


    @ApiOperation(value = "根据流程实例id潜在入册需求信息")
    @PostMapping("/getApprovedReqByProcess")
    public R<WorkflowRsApprovedReqVO> getApprovedReqByProcess(@RequestBody ApprovedRequirementDTO dto) {
        if (StringUtils.isEmpty(dto.getProcessKey())||StringUtils.isEmpty(dto.getSearchId())){
            R.fail("流程实例id为空");
        }
        return R.ok(approvedRequirementService.getApprovedReqByProcess(dto.getSearchId(),dto.getProcessKey()));
    }


    @ApiOperation(value = "新增获批需求")
    @PostMapping("/addApprovedReq")
    public R<String> addApprovedReq(@RequestBody ApprovedRequirementDTO dto) {
        approvedRequirementService.addApprovedReq(dto);
        return R.ok();
    }

    @ApiOperation(value = "新增潜在需求")
    @PostMapping("/addPotentialReq")
    public R<String> addPotentialReq(@RequestBody PotentialRequirementDTO dto) {
        potentialRequirementService.addPotentialReq(dto);
        return R.ok();
    }

    @ApiOperation(value = "更新获批需求")
    @PostMapping("/updateApprovedReq")
    public R<IPage<ApprovedRequirementEntity>> updateApprovedReq(ApprovedRequirementDTO dto) {
        approvedRequirementService.updateApprovedReq(dto);
        return R.ok();
    }

    @ApiOperation(value = "更新潜在需求")
    @PostMapping("/updatePotentialReq")
    public R updatePotentialReq(PotentialRequirementDTO dto) {
        potentialRequirementService.updatePotentialReq(dto);
        return R.ok();
    }

    @ApiOperation(value = "根据id获取获批需求申请表")
    @PostMapping("/getApprovedById")
    public R<ApprovedRequirementDTO> getApprovedById(String id) {
        if (StringUtils.isEmpty(id)){
            R.fail("查询失败");
        }
        return R.ok(approvedRequirementService.getApprovedById(id));
    }

    @ApiOperation(value = "根据id获取潜在需求申请表")
    @PostMapping("/getPotentialById")
    public R<PotentialRequirementDTO> getPotentialById(String id) {
        if (StringUtils.isEmpty(id)){
            R.fail("查询失败");
        }
        return R.ok(potentialRequirementService.getPotentialById(id));
    }
}
