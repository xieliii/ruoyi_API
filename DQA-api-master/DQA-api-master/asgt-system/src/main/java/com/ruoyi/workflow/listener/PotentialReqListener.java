package com.ruoyi.workflow.listener;

import com.ruoyi.srm.supplierBase.domain.VO.PotentialReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.service.PotentialRequirementService;
import com.ruoyi.srm.supplierBase.service.SupplierBaseService;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.constant.ProcessConstant;
import com.ruoyi.workflow.model.dto.ProcessStartListenerDto;
import com.ruoyi.workflow.model.dto.TaskAssigneeListenerDto;
import com.ruoyi.workflow.model.dto.TaskCompleteListenerDto;
import com.ruoyi.workflow.model.dto.TaskRejectListenerDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Axel on 2023/6/14 11:25
 *
 * @author Axel
 */

@Service
@ProcessKey("poentialSupplierEntryProcess")
public class PotentialReqListener implements BusinessWorkflowLifeCycleListener{

    @Resource
    private PotentialRequirementService potentialRequirementService;

    @Resource
    private SupplierBaseService supplierBaseService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T onProcessStart(ProcessStartListenerDto<T> processStartListenerDto) {
        T businessData = processStartListenerDto.getBusinessData();
        String processInstanceId = processStartListenerDto.getProcessInstanceId();
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T onProcessComplete(TaskCompleteListenerDto<T> taskCompleteDto) {
        T businessData = taskCompleteDto.getBusinessData();
        String processInstanceId = taskCompleteDto.getProcessInstanceId();
        PotentialReqVO potentialReq= potentialRequirementService.getPotentialByProcessInstanceId(processInstanceId);
        List<SupplierBaseEntity> suppliers = potentialReq.getSupplierList();
        for (SupplierBaseEntity supplierBaseEntity:suppliers){
            supplierBaseEntity.setCategory(ProcessConstant.POTENTIAL_SUPPLIER);
        }
        supplierBaseService.updateBatchById(suppliers);
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
