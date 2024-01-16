package com.ruoyi.workflow.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.workflow.factory.FlowServiceFactory;
import com.ruoyi.workflow.model.vo.DoneTaskVo;
import com.ruoyi.workflow.model.vo.TaskVo;
import com.ruoyi.workflow.service.WorkflowTaskService;
import com.ruoyi.workflow.utils.ProcessVoUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Axel on 2023/6/9 16:44
 *
 * @author Axel
 */
@Service
public class WorkflowTaskServiceImpl extends FlowServiceFactory implements WorkflowTaskService {
    @Override
    public Page<TaskVo> listPageTask(Integer current, Integer pageSize) {
        if (ObjectUtils.isEmpty(current)) {
            current = 1;
        }
        if(ObjectUtils.isEmpty(pageSize)){
            pageSize = 20;
        }
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active()
                .taskAssignee(s);
        List<Task> list = taskQuery.orderByTaskCreateTime().desc()
                .listPage((current-1)*pageSize, pageSize);
        List<TaskVo> taskVos = ProcessVoUtils.transformFromTask(list);
        Page<TaskVo> page = new Page<>();
        long count = taskQuery.count();
        page.setTotal(count);
        page.setRecords(taskVos);
        page.setCurrent(current);
        page.setSize(pageSize);
        return page;
    }

    @Override
    public Page<TaskVo> listPageTaskByQueryCondition(Integer current, Integer pageSize, String name) {
        if (ObjectUtils.isEmpty(current)) {
            current = 1;
        }
        if(ObjectUtils.isEmpty(pageSize)){
            pageSize = 20;
        }
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        TaskQuery taskQuery = taskService.createTaskQuery()
                .active().taskAssignee(s);
        if(StringUtils.isNotBlank(name)){
            taskQuery = taskQuery
                    .or()
                    .processDefinitionNameLike("%" + name + "%")
                    .taskNameLike("%" + name + "%")
                    .endOr();
        }
        List<Task> list = taskQuery.orderByTaskCreateTime().desc()
                .listPage((current-1)*pageSize, pageSize);
        List<TaskVo> taskVos = ProcessVoUtils.transformFromTask(list);
        Page<TaskVo> page = new Page<>();
        long count = taskQuery.count();
        page.setTotal(count);
        page.setRecords(taskVos);
        page.setCurrent(current);
        page.setSize(pageSize);
        return page;
    }

    @Override
    public Page<DoneTaskVo> listPageFinishedTask(Integer current, Integer pageSize) {
        if (ObjectUtils.isEmpty(current)) {
            current = 1;
        }
        if(ObjectUtils.isEmpty(pageSize)){
            pageSize = 20;
        }
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskAssignee(s);
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()
                .desc()
                .listPage((current-1)*pageSize, pageSize);
        long total = historicTaskInstanceQuery.count();
        List<DoneTaskVo> doneTaskVos = ProcessVoUtils.transformFromFinishedTask(historicTaskInstances);
        Page<DoneTaskVo> page = new Page<>();
        page.setRecords(doneTaskVos);
        page.setTotal(total);
        page.setSize(pageSize);
        page.setCurrent(current);
        return page;
    }

    @Override
    public Page<DoneTaskVo> listPageFinishedTaskByQueryCondition(Integer current, Integer pageSize, String name) {
        if (ObjectUtils.isEmpty(current)) {
            current = 1;
        }
        if(ObjectUtils.isEmpty(pageSize)){
            pageSize = 20;
        }
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                .finished()
                .taskAssignee(s);
        if(StringUtils.isNotBlank(name)){
            historicTaskInstanceQuery = historicTaskInstanceQuery.or().processDefinitionNameLike("%" + name + "%").taskNameLike("%" + name + "%").endOr();
        }
        List<HistoricTaskInstance> historicTaskInstances = historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()
                .desc()
                .listPage((current-1)*pageSize, pageSize);
        long total = historicTaskInstanceQuery.count();
        List<DoneTaskVo> doneTaskVos = ProcessVoUtils.transformFromFinishedTask(historicTaskInstances);
        Page<DoneTaskVo> page = new Page<>();
        page.setRecords(doneTaskVos);
        page.setTotal(total);
        page.setSize(pageSize);
        page.setCurrent(current);
        return page;
    }
}
