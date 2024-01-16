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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ProcessKey("RFQ")
public class RFQListener implements BusinessWorkflowLifeCycleListener{

    @Resource
    private ApprovedRequirementService approvedRequirementService;

    @Resource
    private SupplierBaseService supplierBaseService;

    @Resource
    private SupplierBaseMapper supplierBaseMapper;

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
