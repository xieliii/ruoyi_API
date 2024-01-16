package com.ruoyi.workflow.utils;

import cn.hutool.core.lang.Assert;
import com.google.common.base.Function;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.SnowFlakeUtil;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.workflow.adapter.AbstractBusinessOperationAdapter;
import com.ruoyi.workflow.config.BusinessWorkflowConfig;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.listener.BusinessWorkflowLifeCycleListener;
import com.ruoyi.workflow.model.dto.ProcessStartAdapterDto;
import com.ruoyi.workflow.model.dto.ProcessStartDto;
import com.ruoyi.workflow.model.dto.ProcessStartListenerDto;
import com.ruoyi.workflow.model.vo.ProcessNodeVo;
import com.ruoyi.workflow.model.vo.ProcessStartVo;
import com.ruoyi.workflow.service.WorkflowAssigneeService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Axel on 2023/6/7 11:48
 *
 * @author Axel
 */
@Component
public class WorkflowUtils {
    /**
     * 根据流程实例id获取历史节点
     *
     * @param processInstanceId 流程实例id
     * @return 历史节点
     */
    public static List<String> getHistoricTaskNodes(String processInstanceId) {
        Assert.notBlank(processInstanceId, "流程实例id为空，无法进行流程实例信息查询！");
        HistoryService historyService = SpringUtils.getBean(HistoryService.class);
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByHistoricTaskInstanceEndTime().finished().asc().list();
        Assert.notNull(list, "当前流程实例不存在，无法进行流程实例信息查询！");
        ArrayList<String> arrayList = new ArrayList<>(list.size());
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        } else {
            list.forEach(historicTaskInstance -> arrayList.add(historicTaskInstance.getTaskDefinitionKey()));
        }
        return arrayList.stream().distinct().collect(Collectors.toList());
    }

    public static List<ProcessNodeVo> getHistoricActivityNodes(String processInstanceId){
        Assert.notBlank(processInstanceId, "流程实例id为空，无法进行流程实例信息查询！");
        HistoryService historyService = SpringUtils.getBean(HistoryService.class);
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).finished().orderByHistoricActivityInstanceStartTime().asc().list();
        List<HistoricActivityInstance> list1 = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).unfinished().orderByHistoricActivityInstanceStartTime().asc().list();
        ArrayList<ProcessNodeVo> arrayList = new ArrayList<>(list.size() + list1.size());
        list.forEach(historicActivityInstance -> {
            ProcessNodeVo processNodeVo = new ProcessNodeVo();
            processNodeVo.setKey(historicActivityInstance.getActivityId());
            processNodeVo.setCompleted(true);
            arrayList.add(processNodeVo);
        });
        list1.forEach(historicActivityInstance -> {
            ProcessNodeVo processNodeVo = new ProcessNodeVo();
            processNodeVo.setKey(historicActivityInstance.getActivityId());
            processNodeVo.setCompleted(false);
            arrayList.add(processNodeVo);
        });
        return arrayList;
    }

    /**
     * 根据流程任务id获取已经完成的历史节点信息
     *
     * @param taskId 任务id,活跃节点
     * @return 历史节点
     */
    public static List<String> getHistoricTaskNodeByTaskId(String taskId) {
        Assert.notBlank(taskId, "流程任务id为空，无法进行流程实例信息查询！");
        TaskService taskService = SpringUtils.getBean(TaskService.class);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在，无法获取流程历史信息！");
        String processInstanceId = task.getProcessInstanceId();
        return getHistoricTaskNodes(processInstanceId);
    }

    /**
     * 根据流程实例id从流程定义的层面获取已经经历的流程节点信息
     *
     * @param processInstanceId 流程实例id
     * @return 已通过的流程任务节点
     */
    public static List<UserTask> getHistoricUserTaskNodeByProcessInstanceId(String processInstanceId) {
        Assert.notBlank(processInstanceId, "流程实例id为空，无法获取流程实例详细信息，请重试！");
        List<String> historicTaskNodes = getHistoricTaskNodes(processInstanceId);
        if (CollectionUtils.isEmpty(historicTaskNodes)) {
            return Collections.emptyList();
        } else {
            RuntimeService runtimeService = SpringUtils.getBean(RuntimeService.class);
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            Assert.notNull(processInstance, "当前流程实例不存在，无法获取流程实例详情信息，请重试！");
            String processDefinitionId = processInstance.getProcessDefinitionId();
            List<UserTask> userTasks = listAllUserTaskOfProcessDefinition(processDefinitionId);
            if (userTasks.size() < historicTaskNodes.size()) {
                throw new RuntimeException("当前流程定义不正确，请联系管理员解决!");
            }
            Map<String, UserTask> map = userTasks.stream().collect(Collectors.toMap(UserTask::getId, o -> o));
            return historicTaskNodes.stream().map(new Function<String, UserTask>() {
                @Override
                public @Nullable UserTask apply(@Nullable String input) {
                    return map.get(input);
                }
            }).collect(Collectors.toList());
        }
    }

    /**
     * 根据流程任务id获取流程定义模型中的上一个节点
     *
     * @param taskId 流程任务id
     */
    public static FlowElement getPrevTaskNode(String taskId) {
        Assert.notBlank(taskId, "流程任务id为空，无法进行流程节点信息查询！");
        TaskService taskService = SpringUtils.getBean(TaskService.class);
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在，无法获取流程历史信息！");
        String processDefinitionId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程定义不存在，无法获取到流程定义数据！");
        // 针对单流程，只需获取主流程
        Process process = bpmnModel.getMainProcess();
        Assert.notNull(process, "当前流程定义不存在，请联系管理员解决！");
        List<SequenceFlow> list = process.findFlowElementsOfType(SequenceFlow.class, false);
        Assert.notEmpty(list, "流程定义异常，请联系管理员解决！");
        // 此处仅考虑正常的序列流，不考虑复杂的子流程和网关等
        HashMap<String, FlowElement> map = new HashMap<>(list.size());
        list.forEach(flowElement -> {
            FlowElement sourceFlowElement = flowElement.getSourceFlowElement();
            FlowElement targetFlowElement = flowElement.getTargetFlowElement();
            Assert.notNull(sourceFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            Assert.notNull(targetFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            map.put(targetFlowElement.getId(), sourceFlowElement);
        });
        return map.get(taskDefinitionKey);
    }

    /**
     * 根据当前task的key获取流程定义的下一个节点
     * @param taskDefinitionKey 流程定义id
     * @return 下一个节点信息
     */
    public static FlowElement getNextNodeByTaskDefinitionKey(String taskDefinitionKey, String processDefinitionId){
        if(StringUtils.isAnyBlank(taskDefinitionKey, processDefinitionId)){
            throw new RuntimeException("流程定义id或者流程任务定义key为空，无法进行流程节点信息查询！");
        }
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程定义不存在，无法获取到流程定义数据！");
        // 针对单流程，只需获取主流程
        Process process = bpmnModel.getMainProcess();
        Assert.notNull(process, "当前流程定义不存在，请联系管理员解决！");
        List<SequenceFlow> list = process.findFlowElementsOfType(SequenceFlow.class, false);
        Assert.notEmpty(list, "流程定义异常，请联系管理员解决！");
        // 此处仅考虑正常的序列流，不考虑复杂的子流程和网关等
        HashMap<String, FlowElement> map = new HashMap<>(list.size());
        list.forEach(flowElement -> {
            FlowElement sourceFlowElement = flowElement.getSourceFlowElement();
            FlowElement targetFlowElement = flowElement.getTargetFlowElement();
            Assert.notNull(sourceFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            Assert.notNull(targetFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            map.put(sourceFlowElement.getId(), targetFlowElement);
        });
        return map.get(taskDefinitionKey);
    }

    /**
     * 根据当前task的key获取流程定义的上一个节点
     * @param taskDefinitionKey 流程定义id
     * @return 上一个节点信息
     */
    public static FlowElement getPreNodeByTaskDefinitionKey(String taskDefinitionKey, String processDefinitionId){
        if(StringUtils.isAnyBlank(taskDefinitionKey, processDefinitionId)){
            throw new RuntimeException("流程定义id或者流程任务定义key为空，无法进行流程节点信息查询！");
        }
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程定义不存在，无法获取到流程定义数据！");
        // 针对单流程，只需获取主流程
        Process process = bpmnModel.getMainProcess();
        Assert.notNull(process, "当前流程定义不存在，请联系管理员解决！");
        List<SequenceFlow> list = process.findFlowElementsOfType(SequenceFlow.class, false);
        Assert.notEmpty(list, "流程定义异常，请联系管理员解决！");
        // 此处仅考虑正常的序列流，不考虑复杂的子流程和网关等
        HashMap<String, FlowElement> map = new HashMap<>(list.size());
        list.forEach(flowElement -> {
            FlowElement sourceFlowElement = flowElement.getSourceFlowElement();
            FlowElement targetFlowElement = flowElement.getTargetFlowElement();
            Assert.notNull(sourceFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            Assert.notNull(targetFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            map.put(targetFlowElement.getId(), sourceFlowElement);
        });
        return map.get(taskDefinitionKey);
    }

    /**
     * 根据流程任务id获取流程定义模型中的下一个节点
     *
     * @param taskId 流程任务id
     */
    public static FlowElement getNextTaskNode(String taskId) {
        Assert.notBlank(taskId, "流程任务id为空，无法进行流程节点信息查询！");
        TaskService taskService = SpringUtils.getBean(TaskService.class);
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Assert.notNull(task, "当前流程任务不存在，无法获取流程历史信息！");
        String processDefinitionId = task.getProcessDefinitionId();
        String taskDefinitionKey = task.getTaskDefinitionKey();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程定义不存在，无法获取到流程定义数据！");
        List<Process> processes = bpmnModel.getProcesses();
        Assert.notEmpty(processes, "当前流程定义错误，请联系管理员解决！");
        // 针对单流程，只需获取主流程
        Process process = bpmnModel.getMainProcess();
        Assert.notNull(process, "当前流程定义不存在，请联系管理员解决！");
        List<SequenceFlow> list = process.findFlowElementsOfType(SequenceFlow.class, false);
        Assert.notEmpty(list, "流程定义异常，请联系管理员解决！");
        // 此处仅考虑正常的序列流，不考虑复杂的子流程和网关等
        HashMap<String, FlowElement> map = new HashMap<>(list.size());
        list.forEach(flowElement -> {
            FlowElement sourceFlowElement = flowElement.getSourceFlowElement();
            FlowElement targetFlowElement = flowElement.getTargetFlowElement();
            Assert.notNull(sourceFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            Assert.notNull(targetFlowElement, "流程定义中存在空悬连接，流程定义错误，请联系管理员解决！");
            map.put(sourceFlowElement.getId(), targetFlowElement);
        });
        return map.get(taskDefinitionKey);
    }


    /**
     * 获取流程定义中的所有流程任务节点
     *
     * @param processDefinitionId 流程定义id
     * @return 流程任务节点
     */
    public static List<UserTask> listAllUserTaskOfProcessDefinition(String processDefinitionId) {
        Assert.notBlank(processDefinitionId, "流程定义为空，无法进行流程定义详情数据查询！");
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程定义不存在，无法获取到流程定义数据！");
        Process mainProcess = bpmnModel.getMainProcess();
        Assert.notNull(mainProcess, "当前流程定义错误，请联系管理员解决！");
        return mainProcess.findFlowElementsOfType(UserTask.class, false);
    }

    /**
     * 获取流程定义中的所有流程连接
     *
     * @param processDefinitionId 流程定义id
     * @return 流程连接
     */
    public static List<SequenceFlow> listAllSequenceFlowOfProcessDefinition(String processDefinitionId) {
        Assert.notBlank(processDefinitionId, "流程定义为空，无法进行流程定义详情数据查询！");
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程定义不存在，无法获取到流程定义数据！");
        Process mainProcess = bpmnModel.getMainProcess();
        Assert.notNull(mainProcess, "当前流程定义不存在，请联系管理员解决！");
        return mainProcess.findFlowElementsOfType(SequenceFlow.class, false);
    }

    /**
     * 检查流程任务是否存在，是否为活跃状态可处理
     * @param taskId 任务id
     * @return 任务是否存在
     */
    public static boolean checkTaskExistesAndActive(String taskId){
        Assert.notBlank(taskId, "流程任务id为空，无法进行流程操作！");
        TaskService taskService = SpringUtils.getBean(TaskService.class);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return ObjectUtils.isNotEmpty(task);
    }

    /**
     * 根据流程id获取流程任务id-->name映射细
     * @param processDefinitionId 流程定义id
     * @return 流程任务映射关系
     */
    public static Map<String, String> getUserTaskMapOfProcess(String processDefinitionId){
        List<UserTask> userTasks = listAllUserTaskOfProcessDefinition(processDefinitionId);
        return userTasks.stream().collect(Collectors.toMap(UserTask::getId, UserTask::getName));
    }

    /**
     * 获取流程定义的启动事件
     * @param processDefinitionId 流程定义id
     * @return 流程定义启动事件
     */
    public static StartEvent getStartEventOfProcess(String processDefinitionId){
        Assert.notBlank(processDefinitionId, "流程定义id为空，无法进行启动事件查询！");
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程不存在，取法进行流程启动事件查询");
        Process mainProcess = bpmnModel.getMainProcess();
        List<StartEvent> list = mainProcess.findFlowElementsOfType(StartEvent.class);
        Assert.notEmpty(list, "当前流程定义错误，请联系管理员解决！");
        if(list.size()==1){
            return list.get(0);
        }else{
            throw new RuntimeException("当前流程定义错误，请联系管理员解决！");
        }
    }


    /**
     * 获取流程定义的结束事件
     * @param processDefinitionId 流程定义id
     * @return 流程定义结束事件
     */
    public static EndEvent getEndEventOfProcess(String processDefinitionId){
        Assert.notBlank(processDefinitionId, "流程定义id为空，无法进行结束事件查询！");
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Assert.notNull(bpmnModel, "当前流程不存在，取法进行流程结束事件查询");
        Process mainProcess = bpmnModel.getMainProcess();
        List<EndEvent> list = mainProcess.findFlowElementsOfType(EndEvent.class);
        Assert.notEmpty(list, "当前流程定义错误，请联系管理员解决！");
        if(list.size()==1){
            return list.get(0);
        }else{
            throw new RuntimeException("当前流程定义错误，请联系管理员解决！");
        }
    }

    /**
     * 根据流程定义id获取xml定义数据
     * @param processDefinitionId 流程定义id
     * @return 流程定义xml
     */
    public static String getXmlData(String processDefinitionId){
        if(StringUtils.isBlank(processDefinitionId)){
            throw new RuntimeException("当前流程定义为空，无法获取流程数据！");
        }
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        Assert.notNull(processDefinition, "当前流程定义不存在，无法获取流程定义信息！");
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        try {
            return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("流程定义Xml数据获取失败，请刷新页面后重试！");
        }
    }

    /**
     * 获取启动任务
     * @param processDefinitionId 流程定义id
     * @return 任务
     */
    public static UserTask getFirstUserTaskOfProcess(String processDefinitionId){
        StartEvent startEventOfProcess = getStartEventOfProcess(processDefinitionId);
        List<SequenceFlow> outgoingFlows = startEventOfProcess.getOutgoingFlows();
        Assert.notEmpty(outgoingFlows, "流程定义错误，无法进行流程信息查询！");
        SequenceFlow sequenceFlow = outgoingFlows.get(0);
        Assert.notNull(sequenceFlow, "流程定义错误，无法进行流程信息查询！");
        FlowElement targetFlowElement = sequenceFlow.getTargetFlowElement();
        if(targetFlowElement instanceof UserTask){
            return (UserTask)targetFlowElement;
        }else{
            throw new RuntimeException("当前流程定义错误，无法进行流程信息查询！");
        }
    }

    /**
     * 后端启动流程
     * @param processStartDto 流程数据
     * @param userList 用户列表
     * @param <T> 业务数据类型
     * @return 启动结果
     */
    public static <T> ProcessStartVo<T> startProcessBackend(ProcessStartDto<T> processStartDto, List<AgWorkflowAssigneeEntity> userList) {
        Assert.notNull(processStartDto, "流程启动数据为空，无法正确发起流程!");
        String businessKey = processStartDto.getBusinessKey();
        T businessData = processStartDto.getBusinessData();
        Assert.notBlank(businessKey, "流程业务键为空，无法正确发起流程！");
        Assert.notNull(businessData, "流程业务数据为空，无法正确发起流程！");
        // 认证用户身份并启动流程，并通过第一个流程任务节点
        Long userId = SecurityUtils.getUserId();
        String s = String.valueOf(userId);
        RepositoryService repositoryService = SpringUtils.getBean(RepositoryService.class);
        RuntimeService runtimeService = SpringUtils.getBean(RuntimeService.class);
        IdentityService identityService = SpringUtils.getBean(IdentityService.class);
        WorkflowAssigneeService assigneeService = SpringUtils.getBean(WorkflowAssigneeService.class);
        BusinessWorkflowConfig businessWorkflowConfig = SpringUtils.getBean(BusinessWorkflowConfig.class);
        TaskService taskService = SpringUtils.getBean(TaskService.class);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(businessKey).latestVersion().singleResult();
        Assert.notNull(processDefinition, "当前流程不存在，无法启动流程！");
        identityService.setAuthenticatedUserId(s);
        String key = processDefinition.getKey();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(businessKey);
        Assert.notNull(processInstance, "流程启动失败，请重试！");
        String processInstanceId = processInstance.getProcessInstanceId();
        BusinessWorkflowConfig config = SpringUtils.getBean(BusinessWorkflowConfig.class);
//        AbstractBusinessOperationAdapter businessOperationAdapter = config.createBusinessOperationAdapter(businessKey);
        // 设置流程处理人
        List<Task> list = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        Assert.notEmpty(list, "当前流程启动失败，或者定义异常，请联系管理员解决！");
        if(list.size()>1){
            throw new RuntimeException("当前流程定义异常，请联系管理员解决！");
        }
        Task task = list.get(0);
        Assert.notNull(task, "当前流程启动失败，请重试！");
        String id = task.getId();
        // 添加流程启动意见，并完成第一个节点
        taskService.setAssignee(id, s);
//        List<UserTask> userTasks = WorkflowUtils.listAllUserTaskOfProcessDefinition(processDefinition.getId());
//        // 保存业务数据
//        ProcessStartAdapterDto<Object> objectProcessStartAdapterDto = new ProcessStartAdapterDto<>();
//        objectProcessStartAdapterDto.setProcessKey(key);
//        objectProcessStartAdapterDto.setProcessInstanceId(processInstanceId);
//        objectProcessStartAdapterDto.setBusinessData(businessData);
//        businessOperationAdapter.saveBusinessData(objectProcessStartAdapterDto);
        // 添加流程实例id
        for (Object o : userList) {
            AgWorkflowAssigneeEntity o1 = (AgWorkflowAssigneeEntity) o;
            o1.setProcessInstanceId(processInstanceId);
            o1.setId(SnowFlakeUtil.snowFlakeId());
        }
        assigneeService.saveBatch(userList);
        BusinessWorkflowLifeCycleListener businessWorkflowService = businessWorkflowConfig.createBusinessWorkflowService(businessKey);
        T t = null;
        if(ObjectUtils.isNotEmpty(businessWorkflowService)){
            ProcessStartListenerDto<T> processStartListenerDto = new ProcessStartListenerDto<>();
            processStartListenerDto.setProcessInstanceId(processInstanceId);
            processStartListenerDto.setBusinessData(businessData);
            // 调用声明周期钩子，执行业务逻辑
            t = businessWorkflowService.onProcessStart(processStartListenerDto);
        }
        if(ObjectUtils.isEmpty(t)){
            t = businessData;
        }
        ProcessStartVo<T> tProcessStartVo = new ProcessStartVo<>();
        tProcessStartVo.setBusinessData(t);
        tProcessStartVo.setProcessKey(businessKey);
        tProcessStartVo.setProcessInstanceId(processInstanceId);
        return tProcessStartVo;
    }


    /**
     * 根据流程实例id获取流程发起人
     * @param processInstanceId 流程实例id
     * @return 流程发起人
     */
    public static String getProcessInstanceStarter(String processInstanceId){
        Assert.notEmpty(processInstanceId, "当前流程实例id为空，无法进行流程信息查询！");
        HistoryService historyService = SpringUtils.getBean(HistoryService.class);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Assert.notNull(historicProcessInstance, "当前流程实例不存在，无法进行流程实例信息查询！");
        return historicProcessInstance.getStartUserId();
    }
}
