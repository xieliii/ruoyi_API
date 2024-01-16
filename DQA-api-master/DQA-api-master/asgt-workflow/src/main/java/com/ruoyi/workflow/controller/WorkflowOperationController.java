package com.ruoyi.workflow.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.workflow.model.dto.ProcessStartJsonDto;
import com.ruoyi.workflow.model.dto.TaskAssigneeJsonDto;
import com.ruoyi.workflow.model.dto.TaskCompleteJsonDto;
import com.ruoyi.workflow.model.dto.TaskRejectJsonDto;
import com.ruoyi.workflow.model.vo.ProcessStartVo;
import com.ruoyi.workflow.model.vo.TaskCompleteVo;
import com.ruoyi.workflow.service.WorkflowIntegrateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by Axel on 2023/6/12 15:34
 *
 * @author Axel
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowOperationController {
    @Resource
    private WorkflowIntegrateService workflowIntegrateService;

    @PostMapping("/startProcess")
    public R<Object> startProcess(@RequestBody ProcessStartJsonDto processStartDto){
        ProcessStartVo processStartVo = workflowIntegrateService.startProcess(processStartDto);
        return R.ok(processStartVo);
    }

    @PostMapping("/completeTask")
    public R<Object> completeTask(@RequestBody TaskCompleteJsonDto taskCompleteDto){
        TaskCompleteVo taskCompleteVo = workflowIntegrateService.completeTask(taskCompleteDto);
        return R.ok(taskCompleteVo);
    }

    @PostMapping("/stopProcess")
    public R<Object> stopProcess(@RequestBody TaskCompleteJsonDto taskCompleteDto){
        TaskCompleteVo taskCompleteVo = workflowIntegrateService.stopProcess(taskCompleteDto);
        return R.ok(taskCompleteVo);
    }

    @PostMapping("/rejectProcess")
    public R<Object> rejectProcess(@RequestBody TaskRejectJsonDto taskRejectDto){
        TaskCompleteVo taskCompleteVo = workflowIntegrateService.rejectProcess(taskRejectDto);
        return R.ok(taskCompleteVo);
    }

    @PostMapping("/assigneeTask")
    public R<Object> assigneeTask(@RequestBody TaskAssigneeJsonDto taskAssigneeDto){
        TaskCompleteVo taskCompleteVo = workflowIntegrateService.assigneeTask(taskAssigneeDto);
        return R.ok(taskCompleteVo);
    }

    @PostMapping("/recallTask")
    public R<Object> recallTask(@RequestBody TaskCompleteJsonDto taskCompleteDto){
        TaskCompleteVo taskCompleteVo = workflowIntegrateService.recallTask(taskCompleteDto);
        return R.ok(taskCompleteVo);
    }
}
