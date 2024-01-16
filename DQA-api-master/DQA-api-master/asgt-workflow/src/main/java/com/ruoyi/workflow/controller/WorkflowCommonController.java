package com.ruoyi.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.workflow.model.vo.*;
import com.ruoyi.workflow.service.WorkflowProcessDefinitionService;
import com.ruoyi.workflow.service.WorkflowProcessInstanceService;
import com.ruoyi.workflow.service.WorkflowTaskService;
import org.apache.commons.io.IOUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Axel on 2023/6/12 11:16
 *
 * @author Axel
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowCommonController {
    @Resource
    private WorkflowTaskService workflowTaskService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private WorkflowProcessDefinitionService processDefinitionService;

    @Resource
    private WorkflowProcessInstanceService workflowProcessInstanceService;

    @GetMapping("/listTask")
    public R<Page<TaskVo>> listTask(Integer current, Integer pageSize){
        Page<TaskVo> taskVoPage = workflowTaskService.listPageTask(current, pageSize);
        return R.ok(taskVoPage);
    }

    @GetMapping("/listTaskByQueryCondition")
    public R<Page<TaskVo>> listTaskByQueryCondition(Integer current, Integer pageSize, String name){
        Page<TaskVo> taskVoPage = workflowTaskService.listPageTaskByQueryCondition(current, pageSize, name);
        return R.ok(taskVoPage);
    }

    @GetMapping("/listDoneTask")
    public R<Page<DoneTaskVo>> listDoneTask(Integer current, Integer pageSize){
        Page<DoneTaskVo> doneTaskVoPage = workflowTaskService.listPageFinishedTask(current, pageSize);
        return R.ok(doneTaskVoPage);
    }

    @GetMapping("/listDoneTaskByQueryCondition")
    public R<Page<DoneTaskVo>> listDoneTaskByQueryCondition(Integer current, Integer pageSize, String name){
        Page<DoneTaskVo> doneTaskVoPage = workflowTaskService.listPageFinishedTaskByQueryCondition(current, pageSize, name);
        return R.ok(doneTaskVoPage);
    }

    @GetMapping("/listProcessInstance")
    public R<Page<ProcessInstanceVo>> listProcessInstance(Integer current, Integer pageSize){
        Page<ProcessInstanceVo> processInstanceVoPage = workflowProcessInstanceService.listPageProcessInstance(current, pageSize);
        return R.ok(processInstanceVoPage);
    }

    @GetMapping("/listProcessInstanceByQueryCondition")
    public R<Page<ProcessInstanceVo>> listProcessInstanceByQueryCondition(Integer current, Integer pageSize, String name){
        Page<ProcessInstanceVo> processInstanceVoPage = workflowProcessInstanceService.listPageProcessInstanceByQueryCondition(current, pageSize, name);
        return R.ok(processInstanceVoPage);
    }

    @GetMapping("/getProcessXmlDataByKey")
    public R<String> getProcessXmlDataByKey(String key){
        String xmlDataByKey = processDefinitionService.getXmlDataByKey(key);
        return R.ok(xmlDataByKey);
    }

    @GetMapping("/getProcessDetailById")
    public R<ProcessInstanceDetailVo> getProcessDetailById(String processInstanceId){
        ProcessInstanceDetailVo processInstanceDetailById = workflowProcessInstanceService.getProcessInstanceDetailById(processInstanceId);
        return R.ok(processInstanceDetailById);
    }

    @GetMapping("/listRejecttableTaskOfProcess")
    public R<List<RejecttableTaskVo>> listRejecttableTaskOfProcess(String processInstanceId){
        List<RejecttableTaskVo> rejecttableTaskVos = workflowProcessInstanceService.listRejecttableTaskOfProcess(processInstanceId);
        return R.ok(rejecttableTaskVos);
    }

    @GetMapping("/listProcessStatistic")
    public R<List<ProcessStatisticVo>> listProcessStatistic(){
        List<ProcessStatisticVo> processStatisticVos = workflowProcessInstanceService.listProcessStatistic();
        return R.ok(processStatisticVos);
    }
}
