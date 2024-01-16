package com.ruoyi.workflow.utils;

import cn.hutool.core.lang.Assert;
import com.google.common.base.Function;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.workflow.constant.ProcessInstanceStatusConstant;
import com.ruoyi.workflow.model.vo.DoneTaskVo;
import com.ruoyi.workflow.model.vo.ProcessInstanceVo;
import com.ruoyi.workflow.model.vo.TaskVo;
import org.apache.commons.lang3.ObjectUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Axel on 2023/6/9 17:07
 *
 * @author Axel
 */
public class ProcessVoUtils {
    /**
     * 进行流程引擎Task到前端展示TaskVo的批量转换
     * @param list 流程任务
     * @return 可展示任务数据
     */
    public static  List<TaskVo> transformFromTask(List<Task> list){
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        RepositoryService repositoryService =SpringUtils.getBean(RepositoryService.class);
        HistoryService historyService = SpringUtils.getBean(HistoryService.class);
        ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
        HashSet<String> definitionSet = new HashSet<>();
        HashSet<String> instanceSet = new HashSet<>();
        list.forEach(task -> {
            definitionSet.add(task.getProcessDefinitionId());
            instanceSet.add(task.getProcessInstanceId());
        });
        // 为流程定义信息和流程实例信息准备数据
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(definitionSet).list();
        List<HistoricProcessInstance> instanceList = historyService.createHistoricProcessInstanceQuery().processInstanceIds(instanceSet).list();
        Set<String> startUserSet = instanceList.stream().map(HistoricProcessInstance::getStartUserId).filter(s -> !StringUtils.isBlank(s)).collect(Collectors.toSet());
        Assert.notEmpty(startUserSet, "存在流程启动时未设置正确的流程发起人，请联系管理员解决！");
        List<SysUser> sysUsers = userService.listUserByIdList(startUserSet);
        Map<Long, String> userMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
        Map<String, ProcessDefinition> definitionMap = definitionList.stream().collect(Collectors.toMap(ProcessDefinition::getId, o -> o));
        Map<String, HistoricProcessInstance> instanceMap = instanceList.stream().collect(Collectors.toMap(HistoricProcessInstance::getId, o -> o));
        
        // 生成流程任务的Vo数据
        return list.stream().map(new Function<Task, TaskVo>() {
            @Override
            public @Nullable TaskVo apply(@Nullable Task input) {
                TaskVo tTaskVo = new TaskVo();
                if (input != null) {
                    tTaskVo.setTaskId(input.getId());
                    String processInstanceId = input.getProcessInstanceId();
                    String processDefinitionId = input.getProcessDefinitionId();
                    tTaskVo.setProcessInstanceId(processInstanceId);
                    tTaskVo.setTaskName(input.getName());
                    ProcessDefinition processDefinition = definitionMap.get(processDefinitionId);
                    tTaskVo.setProcessName(processDefinition.getName());
                    tTaskVo.setProcessDefinitionId(processDefinitionId);
                    tTaskVo.setReceiveTime(input.getCreateTime());
                    String startUserId = instanceMap.get(processInstanceId).getStartUserId();
                    tTaskVo.setStartUserId(startUserId);
                    String s = userMap.get(Long.parseLong(startUserId));
                    tTaskVo.setStartUser(s);
                    long duration = System.currentTimeMillis() - input.getCreateTime().getTime();
                    tTaskVo.setDuration(getDate(duration));
                    tTaskVo.setProcessStartTime(instanceMap.get(processInstanceId).getStartTime());
                    tTaskVo.setProcessKey(processDefinition.getKey());
                }
                return tTaskVo;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 进行已办任务转换
     * @param list 流程引擎已办任务
     * @return 已办任务vo
     */
    public static  List<DoneTaskVo> transformFromFinishedTask(List<HistoricTaskInstance> list){
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        RepositoryService repositoryService =SpringUtils.getBean(RepositoryService.class);
        HistoryService historyService = SpringUtils.getBean(HistoryService.class);
        ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
        HashSet<String> definitionSet = new HashSet<>();
        HashSet<String> instanceSet = new HashSet<>();
        list.forEach(task -> {
            definitionSet.add(task.getProcessDefinitionId());
            instanceSet.add(task.getProcessInstanceId());
        });
        // 为流程定义信息和流程实例信息准备数据
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(definitionSet).list();
        List<HistoricProcessInstance> instanceList = historyService.createHistoricProcessInstanceQuery().processInstanceIds(instanceSet).list();
        Set<String> startUserSet = instanceList.stream().map(HistoricProcessInstance::getStartUserId).filter(s -> !StringUtils.isBlank(s)).collect(Collectors.toSet());
        Assert.notEmpty(startUserSet, "存在流程启动时未设置正确的流程发起人，请联系管理员解决！");
        List<SysUser> sysUsers = userService.listUserByIdList(startUserSet);
        Map<Long, String> userMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
        Map<String, ProcessDefinition> definitionMap = definitionList.stream().collect(Collectors.toMap(ProcessDefinition::getId, o -> o));
        Map<String, HistoricProcessInstance> instanceMap = instanceList.stream().collect(Collectors.toMap(HistoricProcessInstance::getId, o -> o));
        return list.stream().map(new Function<HistoricTaskInstance, DoneTaskVo>() {
            @Override
            public @Nullable DoneTaskVo apply(@Nullable HistoricTaskInstance input) {
                DoneTaskVo doneTaskVo = new DoneTaskVo();
                if (ObjectUtils.isNotEmpty(input)) {
                    String processInstanceId = input.getProcessInstanceId();
                    String processDefinitionId = input.getProcessDefinitionId();
                    doneTaskVo.setTaskId(input.getId());
                    doneTaskVo.setProcessDefinitionId(processDefinitionId);
                    doneTaskVo.setProcessInstanceId(processInstanceId);
                    ProcessDefinition processDefinition = definitionMap.get(processDefinitionId);
                    doneTaskVo.setProcessName(processDefinition.getName());
                    doneTaskVo.setTaskName(input.getName());
                    Date createTime = input.getCreateTime();
                    doneTaskVo.setReceiveTime(createTime);
                    Date endTime = input.getEndTime();
                    doneTaskVo.setFinishTime(endTime);
                    long duration = endTime.getTime() - createTime.getTime();
                    doneTaskVo.setDuration(getDate(duration));
                    HistoricProcessInstance historicProcessInstance = instanceMap.get(processInstanceId);
                    doneTaskVo.setProcessStartTime(historicProcessInstance.getStartTime());
                    String startUserId = historicProcessInstance.getStartUserId();
                    doneTaskVo.setStartUserId(startUserId);
                    doneTaskVo.setStartUser(userMap.get(Long.parseLong(startUserId)));
                    doneTaskVo.setProcessKey(processDefinition.getKey());
                }
                return doneTaskVo;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 进行历史发起流程实例到Vo数据的转换
     * @param list 历史流程实例
     * @return 已发起流程实例数据
     */
    public static List<ProcessInstanceVo> transformFromProcessInstance(List<HistoricProcessInstance> list){
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        RepositoryService repositoryService =SpringUtils.getBean(RepositoryService.class);
        TaskService taskService = SpringUtils.getBean(TaskService.class);
        ISysUserService userService = SpringUtils.getBean(ISysUserService.class);
        HashSet<String> definitionSet = new HashSet<>();
        HashSet<String> userSet = new HashSet<>();
        list.forEach(historicProcessInstance -> {
            definitionSet.add(historicProcessInstance.getProcessDefinitionId());
            userSet.add(historicProcessInstance.getStartUserId());
        });
        Assert.notEmpty(userSet, "存在流程启动时未设置正确的流程发起人，请联系管理员解决！");
        // 获取用户信息
        List<SysUser> sysUsers = userService.listUserByIdList(userSet);
        Map<Long, String> userMap = sysUsers.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
        // 获取流程定义信息
        List<ProcessDefinition> definitionList = repositoryService.createProcessDefinitionQuery().processDefinitionIds(definitionSet).list();
        Map<String, ProcessDefinition> definitionMap = definitionList.stream().collect(Collectors.toMap(ProcessDefinition::getId, o -> o));
        // 获取所有流程实例的id列表，进行当前任务查询
        List<String> instanceIdList = list.stream().map(HistoricProcessInstance::getId).collect(Collectors.toList());
        List<Task> taskList = taskService.createTaskQuery().active().processInstanceIdIn(instanceIdList).list();
        Set<String> taskUserList = taskList.stream().map(Task::getAssignee).collect(Collectors.toSet());
        List<SysUser> sysUsers1 = userService.listUserByIdList(taskUserList);
        Map<Long, String> userMap1 = sysUsers1.stream().collect(Collectors.toMap(SysUser::getUserId, SysUser::getUserName));
        HashMap<String, List<Task>> map = new HashMap<>(instanceIdList.size());
        // 根据流程实例id对流程任务进行聚合
        taskList.forEach(task -> {
            String processInstanceId = task.getProcessInstanceId();
            List<Task> tasks = map.get(processInstanceId);
            if (CollectionUtils.isEmpty(tasks)) {
                ArrayList<Task> tasks1 = new ArrayList<>();
                tasks1.add(task);
                map.put(processInstanceId, tasks1);
            }else{
                tasks.add(task);
            }
        });
        // 执行vo转换
        return list.stream().map(new Function<HistoricProcessInstance, ProcessInstanceVo>() {
            @Override
            public @Nullable ProcessInstanceVo apply(@Nullable HistoricProcessInstance input) {
                ProcessInstanceVo processInstanceVo = new ProcessInstanceVo();
                if(!ObjectUtils.isEmpty(input)){
                    String id = input.getId();
                    String processDefinitionId = input.getProcessDefinitionId();
                    ProcessDefinition processDefinition = definitionMap.get(processDefinitionId);
                    processInstanceVo.setProcessName(processDefinition.getName());
                    processInstanceVo.setProcessInstanceId(id);
                    processInstanceVo.setProcessDefinitionId(processDefinitionId);
                    Date startTime = input.getStartTime();
                    processInstanceVo.setStartTime(startTime);
                    Date endTime = input.getEndTime();
                    // 判断流程是否结束，结束才有结束时间
                    if(ObjectUtils.isNotEmpty(endTime)){
                        long time = endTime.getTime() - startTime.getTime();
                        processInstanceVo.setDuration(getDate(time));
                        processInstanceVo.setProcessStatus(ProcessInstanceStatusConstant.FINISHED);
                        processInstanceVo.setFinishTime(endTime);
                    }else{
                        long time = System.currentTimeMillis() - startTime.getTime();
                        processInstanceVo.setDuration(getDate(time));
                        // 挂起状态需要进行考虑，暂未考虑
                        processInstanceVo.setProcessStatus(ProcessInstanceStatusConstant.RUNNING);
                    }
                    String startUserId = input.getStartUserId();
                    processInstanceVo.setStartUser(userMap.get(Long.parseLong(startUserId)));
                    processInstanceVo.setStartUserId(startUserId);
                    processInstanceVo.setProcessKey(processDefinition.getKey());
                    // 获取活跃流程任务信息
                    List<Task> tasks = map.get(id);
                    if(!CollectionUtils.isEmpty(tasks)){
                        Task task = tasks.get(0);
                        String taskDefinitionKey = task.getTaskDefinitionKey();
                        String name = task.getName();
                        String assignee = task.getAssignee();
                        processInstanceVo.setCurrentNode(name);
                        processInstanceVo.setCurrentNodeKey(taskDefinitionKey);
                        if(StringUtils.isNotBlank(assignee)){
                            processInstanceVo.setCurrentAssignee(userMap1.get(Long.parseLong(assignee)));
                        }
                        processInstanceVo.setCurrentAssigneeId(assignee);
                    }
                }
                return processInstanceVo;
            }
        }).collect(Collectors.toList());
    }

    /**
     * 流程完成时间处理
     *
     * @param ms 毫秒数
     * @return 持续时间
     */
    public static String getDate(long ms) {

        long day = ms / (24 * 60 * 60 * 1000);
        long hour = (ms / (60 * 60 * 1000) - day * 24);
        long minute = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (ms / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
        }
    }


}
