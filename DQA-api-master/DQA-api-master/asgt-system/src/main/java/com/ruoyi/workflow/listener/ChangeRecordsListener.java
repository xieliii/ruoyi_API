package com.ruoyi.workflow.listener;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.srm.supplierBase.domain.entity.ChangeRecordsEntity;
import com.ruoyi.srm.supplierBase.domain.entity.SupplierBaseEntity;
import com.ruoyi.srm.supplierBase.mapper.ChangeRecordsMapper;
import com.ruoyi.srm.supplierBase.service.ChangeLogService;
import com.ruoyi.srm.supplierBase.service.SupplierBaseService;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.constant.ProcessConstant;
import com.ruoyi.workflow.model.dto.ProcessStartListenerDto;
import com.ruoyi.workflow.model.dto.TaskAssigneeListenerDto;
import com.ruoyi.workflow.model.dto.TaskCompleteListenerDto;
import com.ruoyi.workflow.model.dto.TaskRejectListenerDto;
import liquibase.pro.packaged.R;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Omelette
 */
@Service
@ProcessKey("SupplierModificationProcess")
public class ChangeRecordsListener implements BusinessWorkflowLifeCycleListener{

    @Resource
    private ChangeLogService changeLogService;

    @Resource
    private ChangeRecordsMapper changeRecordsMapper;

    @Resource
    private SupplierBaseService supplierBaseService;

    /**
     * 业务流程启动事件
     *
     * @param processStartListenerDto 流程启动业务回填数据
     * @return 进行更改之后的业务数据
     */
    @Override
    public <T> T onProcessStart(ProcessStartListenerDto<T> processStartListenerDto) {
        return null;
    }

    /**
     * 业务流程顺利完成事件
     *
     * @param taskCompleteDto 任务完成回填数据
     * @return 业务数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T onProcessComplete(TaskCompleteListenerDto<T> taskCompleteDto) {
        ChangeRecordsEntity entity = changeLogService.getChangeByProcessInstanceId(taskCompleteDto.getProcessInstanceId());
        entity.setApproveFlag(ProcessConstant.YES);
        changeRecordsMapper.updateById(entity);
        String supplierId = entity.getSupplierId();
        SupplierBaseEntity supplierBaseEntity = new SupplierBaseEntity();
        BeanUtils.copyProperties(entity,supplierBaseEntity);
        supplierBaseEntity.setId(supplierId);
        if (Objects.equals(entity.getApproveFlag(),ProcessConstant.YES)){
            supplierBaseService.editSupplierInfo(supplierBaseEntity);
        }
        return null;
    }

    /**
     * 业务流程驳回事件
     *
     * @param taskRejectListenerDto 流程驳回回填数据
     * @return 业务数据
     */
    @Override
    public <T> T onProcessReject(TaskRejectListenerDto<T> taskRejectListenerDto) {
        return null;
    }

    /**
     * 业务流程被拒绝事件
     *
     * @param taskCompleteDto 任务拒绝数据
     * @return 业务数据
     */
    @Override
    public <T> T onProcessStop(TaskCompleteListenerDto<T> taskCompleteDto) {
        ChangeRecordsEntity entity = changeLogService.getChangeByProcessInstanceId(taskCompleteDto.getProcessInstanceId());
        entity.setApproveFlag(ProcessConstant.NO);
        changeRecordsMapper.updateById(entity);
        return null;
    }

    /**
     * 流程任务被通过
     *
     * @param taskCompleteListenerDto 流程通过时回填数据
     * @return 流程完成时回传的业务数据
     */
    @Override
    public <T> T onTaskComplete(TaskCompleteListenerDto<T> taskCompleteListenerDto) {
        return null;
    }

    /**
     * 流程转办
     *
     * @param taskAssigneeListenerDto 流程转办数据
     * @return 业务数据
     */
    @Override
    public <T> T onTaskAssignee(TaskAssigneeListenerDto<T> taskAssigneeListenerDto) {
        return null;
    }

    /**
     * 流程拿回监听事件
     *
     * @param taskCompleteListenerDto 流程任务回填数据
     * @return 流程业务数据返回
     */
    @Override
    public <T> T onTaskRecall(TaskCompleteListenerDto<T> taskCompleteListenerDto) {
        return null;
    }

    @Override
    public <T> T onProcessRevoke() {
        return null;
    }
}
