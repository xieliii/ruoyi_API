package com.ruoyi.workflow.adapter;

import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.model.dto.ProcessStartAdapterDto;
import com.ruoyi.workflow.model.dto.ProcessStartJsonDto;
import org.flowable.bpmn.model.UserTask;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Axel on 2023/6/12 19:37
 *
 * @author Axel
 */
@ProcessKey("srm-process")
@Service
public class BusinessAdapterImpl extends AbstractBusinessOperationAdapter<ProcessStartJsonDto> {
    @Override
    public ProcessStartJsonDto saveBusinessData(ProcessStartAdapterDto<ProcessStartJsonDto> processStartJsonDto) {
        System.out.println("保存业务数据成功");
        System.out.println(processStartJsonDto);
        return null;
    }

    @Override
    public ProcessStartJsonDto updateBusinessData(ProcessStartJsonDto processStartJsonDto) {
        System.out.println("更新业务数据成功");
        System.out.println(processStartJsonDto);
        return null;
    }

    @Override
    public ProcessStartJsonDto getBusinessData(String id) {
        System.out.println("获取业务数据成功");
        System.out.println(id);
        return null;
    }

    @Override
    public List<AgWorkflowAssigneeEntity> saveWorkflowAssignee(List<UserTask> list) {

        return null;
    }

    @Override
    public void deleteBusinessData(String id) {
        System.out.println("删除业务数据成功！");
    }


    //    @Override
//    public ProcessStartJsonDto createEntity(String jsonStr) {
//        Assert.notEmpty(jsonStr, "业务数据为空，无法进行业务操作！");
//        return JSONObject.parseObject(jsonStr, ProcessStartJsonDto.class);
//    }
}
