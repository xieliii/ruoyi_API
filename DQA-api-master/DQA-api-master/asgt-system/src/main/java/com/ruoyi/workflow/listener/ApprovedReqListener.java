package com.ruoyi.workflow.listener;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.DeptConstant;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.srm.supplierBase.domain.DTO.ApprovedRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.VO.ApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.VO.WorkflowRsApprovedReqVO;
import com.ruoyi.srm.supplierBase.domain.entity.ApprovedRequirementEntity;
import com.ruoyi.srm.supplierBase.mapper.ApprovedRequirementMapper;
import com.ruoyi.srm.supplierBase.service.ApprovedRequirementService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.constant.ProcessConstant;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.model.dto.*;
import com.ruoyi.workflow.model.vo.ProcessStartVo;
import com.ruoyi.workflow.service.WorkflowIntegrateService;
import com.ruoyi.workflow.utils.WorkflowUtils;
import liquibase.pro.packaged.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Omelette
 */
@Service
@ProcessKey("approvedSupplierEntryProcess")
public class ApprovedReqListener implements BusinessWorkflowLifeCycleListener{

    @Resource
    private ApprovedRequirementService approvedRequirementService;

    @Resource
    private ApprovedRequirementMapper approvedRequirementMapper;

    @Resource
    private ISysUserService userService;

    @Resource
    private ISysDeptService deptService;

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
        String processInstanceId = taskCompleteDto.getProcessInstanceId();
        WorkflowRsApprovedReqVO approvedReq= approvedRequirementService.getApprovedByProcessInstanceId(processInstanceId);
        // 业务数据
        ProcessStartDto<T> processStartDto = new ProcessStartDto();
        T businessData = taskCompleteDto.getBusinessData();
        processStartDto.setBusinessData(businessData);
        // 新增一条数据
        String processInstanceStarter = WorkflowUtils.getProcessInstanceStarter(processInstanceId);
        Long starter = Long.valueOf(processInstanceStarter);
        SysDept userDept = deptService.selectDeptById(userService.selectUserById(starter).getDeptId());
        SysDept sysDept = deptService.selectDeptById(DeptConstant.SUPPLIER_DEPT_ID);
        Map<String,String> nodeAndAssigneeMap = new HashMap<>();
        ArrayList<AgWorkflowAssigneeEntity> arrayList = new ArrayList<>();
        // 进入设计类RFQ流程
        if (Objects.equals(approvedReq.getCategory(),ProcessConstant.CATEGORY5)||Objects.equals(approvedReq.getCategory(),ProcessConstant.CATEGORY7)){
            // TODO 流程处理人信息
            nodeAndAssigneeMap.put(ProcessConstant.RFQ_NODE_ONE,processInstanceStarter);
            nodeAndAssigneeMap.put(ProcessConstant.RFQ_NODE_TWO,String.valueOf(userDept.getManager()));
            nodeAndAssigneeMap.put(ProcessConstant.RFQ_NODE_THREE,String.valueOf(starter));
            nodeAndAssigneeMap.put(ProcessConstant.RFQ_NODE_FOUR,String.valueOf(sysDept.getLeader()));
            nodeAndAssigneeMap.put(ProcessConstant.RFQ_NODE_FIVE,String.valueOf(starter));
            nodeAndAssigneeMap.put(ProcessConstant.RFQ_NODE_SIX,String.valueOf(sysDept.getLeader()));
            nodeAndAssigneeMap.forEach((key,value) -> {
                AgWorkflowAssigneeEntity agWorkflowAssigneeEntity = new AgWorkflowAssigneeEntity();
                agWorkflowAssigneeEntity.setAssignee(value);
                agWorkflowAssigneeEntity.setProcessKey(ProcessConstant.RFQ_PROCESS_KEY);
                agWorkflowAssigneeEntity.setTaskNodeKey(key);
                arrayList.add(agWorkflowAssigneeEntity);
            });
            processStartDto.setBusinessKey(ProcessConstant.RFQ_PROCESS_KEY);
            ProcessStartVo processStartVo = WorkflowUtils.startProcessBackend(processStartDto, arrayList);
            approvedReq.setRfqProcessInstanceId(processStartVo.getProcessInstanceId());
        }
        // 开启RFI
        else {
            // TODO 流程处理人信息
            nodeAndAssigneeMap.put(ProcessConstant.RFI_NODE_ONE,processInstanceStarter);
            nodeAndAssigneeMap.put(ProcessConstant.RFI_NODE_TWO,String.valueOf(userDept.getManager()));
            nodeAndAssigneeMap.put(ProcessConstant.RFI_NODE_THREE,String.valueOf(starter));
            nodeAndAssigneeMap.put(ProcessConstant.RFI_NODE_FOUR,String.valueOf(sysDept.getLeader()));
            nodeAndAssigneeMap.put(ProcessConstant.RFI_NODE_FIVE,String.valueOf(starter));
            nodeAndAssigneeMap.put(ProcessConstant.RFI_NODE_SIX,String.valueOf(sysDept.getLeader()));
            nodeAndAssigneeMap.forEach((key,value) -> {
                AgWorkflowAssigneeEntity agWorkflowAssigneeEntity = new AgWorkflowAssigneeEntity();
                agWorkflowAssigneeEntity.setAssignee(value);
                agWorkflowAssigneeEntity.setProcessKey(ProcessConstant.RFI_PROCESS_KEY);
                agWorkflowAssigneeEntity.setTaskNodeKey(key);
                arrayList.add(agWorkflowAssigneeEntity);
            });

            processStartDto.setBusinessKey(ProcessConstant.RFI_PROCESS_KEY);
            ProcessStartVo processStartVo = WorkflowUtils.startProcessBackend(processStartDto, arrayList);
            approvedReq.setRfiProcessInstanceId(processStartVo.getProcessInstanceId());
        }
        ApprovedRequirementEntity approvedRequirementEntity = new ApprovedRequirementEntity();
        BeanUtils.copyProperties(approvedReq,approvedRequirementEntity);
        approvedRequirementMapper.updateById(approvedRequirementEntity);
        return businessData;
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
    @Transactional(rollbackFor = Exception.class)
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
