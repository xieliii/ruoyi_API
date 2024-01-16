package com.ruoyi.workflow.listener;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ruoyi.srm.supplierBase.domain.VO.ApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.VO.WorkflowRsApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierListEntity;
import com.ruoyi.srm.supplierBase.mapper.SupplierBaseMapper;
import com.ruoyi.srm.supplierBase.service.ApprovedRequirementService;
import com.ruoyi.srm.supplierBase.service.SupplierBaseService;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.constant.ProcessConstant;
import com.ruoyi.workflow.model.dto.ProcessStartListenerDto;
import com.ruoyi.workflow.model.dto.TaskAssigneeListenerDto;
import com.ruoyi.workflow.model.dto.TaskCompleteListenerDto;
import com.ruoyi.workflow.model.dto.TaskRejectListenerDto;
import liquibase.pro.packaged.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: paurs
 * @date: 2023/6/21 15:22
 */
@Service
@ProcessKey("RFP")
public class RFPListener implements BusinessWorkflowLifeCycleListener{

    @Resource
    private ApprovedRequirementService approvedRequirementService;

    @Resource
    private SupplierBaseService supplierBaseService;

    @Resource
    private SupplierBaseMapper supplierBaseMapper;

    @Override
    public <T> T onProcessStart(ProcessStartListenerDto<T> processStartListenerDto) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T onProcessComplete(TaskCompleteListenerDto<T> taskCompleteDto) {
        WorkflowRsApprovedReqVO approvedReq= approvedRequirementService.getApprovedByProcessInstanceId(taskCompleteDto.getProcessInstanceId());
        List<SupplierListEntity> suppliers =  approvedReq.getSupplierList();
        List<String> supplierIds = suppliers.stream().map(SupplierListEntity::getSupplierId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(supplierIds)){
            List<SupplierBaseEntity> baseEntities = supplierBaseMapper.selectBatchIds(supplierIds);
            for (SupplierBaseEntity baseEntity:baseEntities){
                baseEntity.setCategory(ProcessConstant.APPROVED_SUPPLIER);
            }
            supplierBaseService.updateBatchById(baseEntities);
        }
        return null;
    }

    @Override
    public <T> T onProcessReject(TaskRejectListenerDto<T> taskRejectListenerDto) {
        return null;
    }

    @Override
    public <T> T onProcessStop(TaskCompleteListenerDto<T> taskCompleteDto) {
        return null;
    }

    @Override
    public <T> T onTaskComplete(TaskCompleteListenerDto<T> taskCompleteListenerDto) {
        return null;
    }

    @Override
    public <T> T onTaskAssignee(TaskAssigneeListenerDto<T> taskAssigneeListenerDto) {
        return null;
    }

    @Override
    public <T> T onTaskRecall(TaskCompleteListenerDto<T> taskCompleteListenerDto) {
        return null;
    }

    @Override
    public <T> T onProcessRevoke() {
        return null;
    }
}
