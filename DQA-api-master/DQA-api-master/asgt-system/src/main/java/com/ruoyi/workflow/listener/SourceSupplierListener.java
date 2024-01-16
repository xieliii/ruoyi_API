package com.ruoyi.workflow.listener;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.srm.supplierBase.domain.VO.SupplierBaseVO;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
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

/**
 * @author: parus
 * @date: 2023/6/21 15:22
 */
@Service
@ProcessKey("SourceSupplierEntryProcess")
public class SourceSupplierListener implements BusinessWorkflowLifeCycleListener{

    @Resource
    private SupplierBaseService supplierBaseService;

    @Override
    public <T> T onProcessStart(ProcessStartListenerDto<T> processStartListenerDto) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T onProcessComplete(TaskCompleteListenerDto<T> taskCompleteDto) {
        SupplierBaseVO supplierVO = supplierBaseService.getSupplierByProcessInstanceId(taskCompleteDto.getProcessInstanceId());
        SupplierBaseEntity supplierBaseEntity = new SupplierBaseEntity();
        BeanUtils.copyProperties(supplierVO,supplierBaseEntity);
        supplierBaseEntity.setAbleFlag(ProcessConstant.YES);
        supplierBaseService.updateById(supplierBaseEntity);
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
