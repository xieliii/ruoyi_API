package com.ruoyi.workflow.adpter;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.constant.DeptConstant;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.srm.supplierBase.domain.DTO.PotentialRequirementDTO;
import com.ruoyi.srm.supplierBase.service.PotentialRequirementService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.UserService;
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
 * Created by Axel on 2023/6/14 11:21
 * 尽在在类上使用事务注解，或导致问题出现
 * @author Axel
 */

@Service
@ProcessKey("poentialSupplierEntryProcess")
public class PotentialReqAdapter extends AbstractBusinessOperationAdapter<PotentialRequirementDTO> {

    @Resource
    private PotentialRequirementService potentialRequirementService;

    @Resource
    private ISysDeptService deptService;

    @Resource
    private ISysUserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PotentialRequirementDTO saveBusinessData(ProcessStartAdapterDto<PotentialRequirementDTO> startAdapterDto) {
        Assert.notNull(startAdapterDto, ProcessConstant.NOT_EXIST);
        PotentialRequirementDTO businessData = startAdapterDto.getBusinessData();
        businessData.setProcessInstanceId(startAdapterDto.getProcessInstanceId());
        potentialRequirementService.addPotentialReq(businessData);
        return businessData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PotentialRequirementDTO updateBusinessData(PotentialRequirementDTO potentialRequirementDTO) {
        potentialRequirementService.updatePotentialReq(potentialRequirementDTO);
        return potentialRequirementDTO;
    }

    @Override
    public PotentialRequirementDTO getBusinessData(String id) {
        return null;
    }

    @Override
    public void deleteBusinessData(String id) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<AgWorkflowAssigneeEntity> saveWorkflowAssignee(List<UserTask> list) {
        Map<String,String> nodeAndAssigneeMap = new HashMap<>(list.size());
        Long userId = SecurityUtils.getUserId();
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_ONE,String.valueOf(userId));
        SysDept userDept = deptService.selectDeptById(userService.selectUserById(userId).getDeptId());
        SysDept sysDept = deptService.selectDeptById(DeptConstant.SUPPLIER_DEPT_ID);
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_TWO,String.valueOf(userDept.getManager()));
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_THREE,String.valueOf(sysDept.getManager()));
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_FOUR,String.valueOf(sysDept.getLeader()));
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_FIVE,String.valueOf(sysDept.getManager()));
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_SIX,String.valueOf(sysDept.getLeader()));
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_SEVEN,String.valueOf(sysDept.getManager()));
        nodeAndAssigneeMap.put(ProcessConstant.POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_EIGHT,String.valueOf(sysDept.getLeader()));
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
