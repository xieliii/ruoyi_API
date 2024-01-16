package com.ruoyi.workflow.adpter;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.constant.DeptConstant;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.srm.supplierBase.domain.DTO.ApprovedRequirementDTO;
import com.ruoyi.srm.supplierBase.domain.DTO.ClearRecordsDTO;
import com.ruoyi.srm.supplierBase.service.ApprovedRequirementService;
import com.ruoyi.srm.supplierBase.service.ClearRecordsService;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.workflow.adapter.AbstractBusinessOperationAdapter;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.constant.ProcessConstant;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.model.dto.ProcessStartAdapterDto;
import liquibase.pro.packaged.R;
import org.flowable.bpmn.model.UserTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: parus
 * @date: 2023/6/21 15:20
 */
@Service
@ProcessKey("supplierClearProcess")
public class ClearRecordsAdapter extends AbstractBusinessOperationAdapter<ClearRecordsDTO> {
    @Resource
    private ClearRecordsService clearRecordsService;

    @Resource
    private ISysDeptService deptService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClearRecordsDTO saveBusinessData(ProcessStartAdapterDto<ClearRecordsDTO> startAdapterDto) {
        Assert.notNull(startAdapterDto, ProcessConstant.NOT_EXIST);
        ClearRecordsDTO businessData = startAdapterDto.getBusinessData();
        businessData.setProcessInstanceId(startAdapterDto.getProcessInstanceId());
        clearRecordsService.addClearRecords(businessData);
        return businessData;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ClearRecordsDTO updateBusinessData(ClearRecordsDTO clearRecordsDTO) {
        clearRecordsService.addClearRecords(clearRecordsDTO);
        return clearRecordsDTO;
    }

    @Override
    public ClearRecordsDTO getBusinessData(String id) {
        return null;
    }

    @Override
    public void deleteBusinessData(String id) {

    }

    @Override
    public List<AgWorkflowAssigneeEntity> saveWorkflowAssignee(List<UserTask> list) {
        Map<String,String> nodeAndAssigneeMap = new HashMap<>(list.size());
        Long userId = SecurityUtils.getUserId();
        nodeAndAssigneeMap.put(ProcessConstant.SUPPLIER_CLEAR_PROCESS_NODE_ONE,String.valueOf(userId));
        SysDept sysDept = deptService.selectDeptById(DeptConstant.SUPPLIER_DEPT_ID);
        nodeAndAssigneeMap.put(ProcessConstant.SUPPLIER_CLEAR_PROCESS_NODE_TWO,String.valueOf(sysDept.getLeader()));
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

