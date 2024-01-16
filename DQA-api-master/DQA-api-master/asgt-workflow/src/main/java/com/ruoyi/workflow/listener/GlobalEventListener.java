package com.ruoyi.workflow.listener;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.factory.FlowServiceFactory;
import com.ruoyi.workflow.service.WorkflowAssigneeService;
import com.ruoyi.workflow.utils.WorkflowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.api.delegate.event.FlowableEventType;
import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Axel
 */
@Slf4j
@Component
public class GlobalEventListener extends FlowServiceFactory implements FlowableEventListener {
    @Resource
    private WorkflowAssigneeService assigneeService;
    @Override
    public void onEvent(FlowableEvent flowableEvent) {
        FlowableEventType type = flowableEvent.getType();
        if(type == FlowableEngineEventType.TASK_CREATED){
            // 第一个任务不用设置，此时也查询不到assignee数据
            if(flowableEvent instanceof FlowableEntityEventImpl){
                TaskEntity taskEntity = (TaskEntity) ((FlowableEntityEventImpl) flowableEvent).getEntity();
                String id = taskEntity.getId();
                String taskDefinitionKey = taskEntity.getTaskDefinitionKey();
                String processDefinitionId = taskEntity.getProcessDefinitionId();
                String processInstanceId = taskEntity.getProcessInstanceId();
                FlowElement taskNode = WorkflowUtils.getPreNodeByTaskDefinitionKey(taskDefinitionKey, processDefinitionId);
                Assert.notNull(taskNode, "当前流程定义错误，无法进行流程操作，请联系管理员解决！");
                if(taskNode instanceof StartEvent){
                    long count = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).count();
                    // 判断是否是发起时还是被驳回时
                    if(count>1){
                        LambdaQueryWrapper<AgWorkflowAssigneeEntity> wrapper = Wrappers.lambdaQuery(AgWorkflowAssigneeEntity.class)
                                .eq(AgWorkflowAssigneeEntity::getProcessInstanceId, processInstanceId);
                        List<AgWorkflowAssigneeEntity> list = assigneeService.list(wrapper);
                        Assert.notEmpty(list, "当前流程实例查询不到流程流转人员信息，无法进行流程操作！");
                        Map<String, String> collect = list.stream().collect(Collectors.toMap(AgWorkflowAssigneeEntity::getTaskNodeKey, AgWorkflowAssigneeEntity::getAssignee));
                        String s = collect.get(taskDefinitionKey);
                        if(StringUtils.isBlank(s)){
                            throw new RuntimeException("未查询到正确的流程处理人信息，无法进行流程操作！");
                        }
                        taskService.setAssignee(id, s);
                    }
                }else{
                    LambdaQueryWrapper<AgWorkflowAssigneeEntity> wrapper = Wrappers.lambdaQuery(AgWorkflowAssigneeEntity.class)
                            .eq(AgWorkflowAssigneeEntity::getProcessInstanceId, processInstanceId);
                    List<AgWorkflowAssigneeEntity> list = assigneeService.list(wrapper);
                    Assert.notEmpty(list, "当前流程实例查询不到流程流转人员信息，无法进行流程操作！");
                    Map<String, String> collect = list.stream().collect(Collectors.toMap(AgWorkflowAssigneeEntity::getTaskNodeKey, AgWorkflowAssigneeEntity::getAssignee));
                    String s = collect.get(taskDefinitionKey);
                    if(StringUtils.isBlank(s)){
                        throw new RuntimeException("未查询到正确的流程处理人信息，无法进行流程操作！");
                    }
                    taskService.setAssignee(id, s);
                }
                System.out.println("taskId:" + id);
                System.out.println("processInstanceId:" + processInstanceId);
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return true;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}