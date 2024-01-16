package com.ruoyi.srm.contractMgt.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.srm.contractMgt.domain.DTO.ContractDTO;
import com.ruoyi.srm.contractMgt.domain.VO.ContractRsVO;
import com.ruoyi.srm.contractMgt.domain.entity.ContractEntity;
import com.ruoyi.srm.contractMgt.service.ContractService;
import com.ruoyi.srm.supplierBase.domain.DTO.ApprovedRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.PotentialRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.entity.ApprovedRequirementEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Omelette
 */
@Api(value = "需求接口")
@RestController
@RequestMapping("/contract-mgt")
public class ContractController {
    @Resource
    private ContractService contractService;

    @ApiOperation(value = "查询合同 分页")
    @PostMapping("/getContractByPage")
    public R<IPage<ContractRsVO>> getContractByPage(@RequestBody ContractDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            R.fail("获取失败");
        }
        return R.ok(contractService.getContractByPage(dto));
    }

    @ApiOperation(value = "新增合同")
    @PostMapping("/addContract")
    public R<Boolean> addContract(@RequestBody ContractDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            R.fail("新增失败");
        }
        return R.ok(contractService.addContract(dto));
    }

    @ApiOperation(value = "更新合同")
    @PostMapping("/updateContract")
    public R<Integer> updateContract(@RequestBody ContractDTO dto) {
        if (ObjectUtils.isEmpty(dto)){
            R.fail("更新失败");
        }
        return R.ok(contractService.updateContract(dto));
    }

    @ApiOperation(value = "根据id获取合同详情")
    @PostMapping("/getContractById")
    public R<ContractEntity> getContractById(@RequestParam String id) {
        if (StringUtils.isEmpty(id)){
            R.fail("查询失败");
        }
        return R.ok(contractService.getContractById(id));
    }
}
