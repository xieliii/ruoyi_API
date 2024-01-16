package com.ruoyi.workflow.adpter;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.constant.DeptConstant;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.srm.supplierBase.domain.DTO.ApprovedRequirementDTO;
import com.ruoyi.srm.supplierBase.service.ApprovedRequirementService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.adapter.AbstractBusinessOperationAdapter;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.constant.ProcessConstant;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.model.dto.ProcessStartAdapterDto;
import org.flowable.bpmn.model.UserTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omelette
 */
@Service
@ProcessKey("approvedSupplierEntryProcess")
public class ApprovedReqAdapter extends AbstractBusinessOperationAdapter<ApprovedRequirementDTO> {
    @Resource
    private ApprovedRequirementService approvedRequirementService;

    @Resource
    private ISysDeptService deptService;

    @Resource
    private ISysUserService userService;

    /**
     * 流程业务适配器，用于保存业务数据
     *
     * @param adapterDto 业务数据
     * @return 保存后的业务数据，返回的数据中必须包含id,
     * 用于唯一标识业务数据，类似于表单id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovedRequirementDTO saveBusinessData(ProcessStartAdapterDto<ApprovedRequirementDTO> adapterDto) {
        Assert.notNull(adapterDto, ProcessConstant.NOT_EXIST);
        ApprovedRequirementDTO businessData = adapterDto.getBusinessData();
        businessData.setProcessInstanceId(adapterDto.getProcessInstanceId());
        approvedRequirementService.addApprovedReq(businessData);
        return businessData;
    }

    /**
     * 更新业务数据接口
     *
     * @param approvedRequirementDTO 业务数据
     * @return 更新后的业务数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApprovedRequirementDTO updateBusinessData(ApprovedRequirementDTO approvedRequirementDTO) {
        approvedRequirementService.updateApprovedReq(approvedRequirementDTO);
        return approvedRequirementDTO;
    }

    /**
     * 根据业务id获取流程业务数据
     *
     * @param id 业务id
     * @return 业务数据
     */
    @Override
    public ApprovedRequirementDTO getBusinessData(String id) {
        return null;
    }

    /**
     * 根据业务id删除业务数据
     *
     * @param id 业务id
     */
    @Override
    public void deleteBusinessData(String id) {

    }

    /**
     * 插入流程处理人信息
     *
     * @param list 流程处理人信息
     * @return 流程处理人数据
     */
    @Override
    public List<AgWorkflowAssigneeEntity> saveWorkflowAssignee(List<UserTask> list) {
        Map<String,String> nodeAndAssigneeMap = new HashMap<>(list.size());
        Long userId = SecurityUtils.getUserId();
        nodeAndAssigneeMap.put(ProcessConstant.APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_ONE,String.valueOf(userId));
        SysDept sysDept = deptService.selectDeptById(DeptConstant.SUPPLIER_DEPT_ID);
        nodeAndAssigneeMap.put(ProcessConstant.APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_TWO,String.valueOf(sysDept.getLeader()));
        nodeAndAssigneeMap.put(ProcessConstant.APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_THREE,String.valueOf(sysDept.getManager()));
        nodeAndAssigneeMap.put(ProcessConstant.APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_FOUR,String.valueOf(sysDept.getLeader()));
        ArrayList<AgWorkflowAssigneeEntity> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AgWorkflowAssigneeEntity agWorkflowAssigneeEntity = new AgWorkflowAssigneeEntity();
            agWorkflowAssigneeEntity.setAssignee(nodeAndAssigneeMap.get(list.get(i).getId()));
            agWorkflowAssigneeEntity.setProcessKey(ProcessConstant.APPROVED_PROCESS_KEY);
            agWorkflowAssigneeEntity.setTaskNodeKey(list.get(i).getId());
            arrayList.add(agWorkflowAssigneeEntity);
        }
        return arrayList;
    }
}
