package com.ruoyi.workflow.listener;

import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.srm.supplierBase.domain.VO.ApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.VO.ClearRecordsVO;
import com.ruoyi.srm.supplierBase.domain.entity.ClearRecordsEntity;
import com.ruoyi.srm.supplierBase.service.ClearRecordsService;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.model.dto.ProcessStartListenerDto;
import com.ruoyi.workflow.model.dto.TaskAssigneeListenerDto;
import com.ruoyi.workflow.model.dto.TaskCompleteListenerDto;
import com.ruoyi.workflow.model.dto.TaskRejectListenerDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: parus
 * @date: 2023/6/21 15:22
 */
@Service
@ProcessKey("supplierClearProcess")
public class ClearRecordsListener implements BusinessWorkflowLifeCycleListener{

    @Resource
    private ClearRecordsService clearRecordsService;

    @Override
    public <T> T onProcessStart(ProcessStartListenerDto<T> processStartListenerDto) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T onProcessComplete(TaskCompleteListenerDto<T> taskCompleteDto) {
        String processInstanceId = taskCompleteDto.getProcessInstanceId();
        ClearRecordsVO clearRecordsVO= clearRecordsService.getClearByProcessInstanceId(processInstanceId);
        Date date = new Date();
        clearRecordsVO.setCompleteTime(date);
        ClearRecordsEntity entity = new ClearRecordsEntity();
        BeanUtils.copyProperties(clearRecordsVO,entity);
        clearRecordsService.updateById(entity);
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
