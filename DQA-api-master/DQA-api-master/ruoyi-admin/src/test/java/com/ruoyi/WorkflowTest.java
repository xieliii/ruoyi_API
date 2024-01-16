//package com.ruoyi;
//
//import cn.hutool.core.annotation.AnnotationUtil;
//import cn.hutool.core.util.ReflectUtil;
//import cn.xuyanwu.spring.file.storage.FileInfo;
//import cn.xuyanwu.spring.file.storage.FileStorageProperties;
//import cn.xuyanwu.spring.file.storage.FileStorageService;
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONObject;
//import com.alibaba.fastjson2.util.AnnotationUtils;
//import com.ruoyi.common.utils.reflect.ReflectUtils;
//import com.ruoyi.common.utils.spring.SpringUtils;
//import com.ruoyi.workflow.adapter.BusinessAdapterImpl;
//import com.ruoyi.workflow.annotaions.ProcessKey;
//import com.ruoyi.workflow.domain.AgFileDetailEntity;
//import com.ruoyi.workflow.model.dto.ProcessStartDto;
//import com.ruoyi.workflow.model.dto.ProcessStartJsonDto;
//import com.ruoyi.workflow.model.vo.ProcessInstanceVo;
//import com.ruoyi.workflow.model.vo.ProcessStartVo;
//import com.ruoyi.workflow.model.vo.TaskVo;
//import com.ruoyi.workflow.service.WorkflowIntegrateService;
//import com.ruoyi.workflow.service.WorkflowTaskService;
//import com.ruoyi.workflow.utils.ProcessVoUtils;
//import com.ruoyi.workflow.utils.WorkflowUtils;
//import lombok.SneakyThrows;
//import org.apache.commons.io.IOUtils;
//import org.flowable.bpmn.model.*;
//import org.flowable.bpmn.model.Process;
//import org.flowable.engine.*;
//import org.flowable.engine.history.HistoricProcessInstance;
//import org.flowable.engine.repository.Deployment;
//import org.flowable.engine.repository.ProcessDefinition;
//import org.flowable.engine.runtime.ProcessInstance;
//import org.flowable.engine.task.Comment;
//import org.flowable.task.api.Task;
//import org.flowable.task.api.history.HistoricTaskInstance;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
//import org.springframework.core.type.classreading.MetadataReader;
//import org.springframework.core.type.classreading.MetadataReaderFactory;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.ClassUtils;
//
//import javax.annotation.Resource;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Axel on 2023/6/1 11:32
// *
// * @author Axel
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class WorkflowTest {
//
//    @Resource
//    private RepositoryService repositoryService;
//    @Resource
//    private FileStorageService fileStorageService;
//    @Resource
//    private RuntimeService runtimeService;
//    @Resource
//    private TaskService taskService;
//    @Resource
//    private HistoryService historyService;
//    @Resource
//    private WorkflowTaskService workflowTaskService;
//    @Resource
//    private IdentityService identityService;
//    @Resource
//    private WorkflowIntegrateService workflowIntegrateService;
//
//    @Test
//    public void test(){
////        System.out.println("test");
////        Deployment deploy = repositoryService.createDeployment().addClasspathResource("process/srmSource.bpmn20.xml").name("潜在供应商入册流程").deploy();
////        System.out.println(deploy);
////        System.out.println(deploy.getId());
////        System.out.println(deploy.getParentDeploymentId());
////        System.out.println(deploy.getName());
//    }
//
//    @Test
//    public void test2(){
//        List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymentTime().desc().list();
//        System.out.println(list);
//    }
//
//    @Test
//    public void test3(){
//        repositoryService.deleteDeployment("f3e5e9d8-003d-11ee-aaa9-00ff1cab5def", true);
//    }
//
//    @Test
//    public void test1(){
//        long count = repositoryService.createProcessDefinitionQuery().active().count();
//        System.out.println(count);
//    }
//
//    @Test
//    public void test4(){
//        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
//        List<ProcessDefinition> list1 = repositoryService.createProcessDefinitionQuery().list();
//        System.out.println(list);
//        System.out.println(list1);
//    }
//
//    @Test
//    public void test5(){
//        FileStorageProperties properties = fileStorageService.getProperties();
//        System.out.println(properties);
//        ClassPathResource classPathResource = new ClassPathResource("banner.txt");
//        try {
//            InputStream inputStream = classPathResource.getInputStream();
//            FileInfo upload = fileStorageService.of(inputStream)
//                    .setName("banner.txt")
//                    .setSaveFilename("banner.txt")
//                    .setOriginalFilename("banner.txt")
//                    .upload();
//            System.out.println(upload);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void test6(){
//        FileInfo fileInfoByUrl = fileStorageService.getFileInfoByUrl("srm/banner.txt");
//        System.out.println(fileInfoByUrl);
//        boolean exists = fileStorageService.exists(fileInfoByUrl);
//
//        System.out.println(exists);
//    }
//
//    @Test
//    public void test7(){
////        FileInfo fileInfoByUrl = fileStorageService.getFileInfoByUrl("srm/banner1.txt");
////        System.out.println(fileInfoByUrl);
////        boolean exists = fileStorageService.exists(fileInfoByUrl);
////        System.out.println(exists);
//        fileStorageService.delete("srm/banner1.txt");
//    }
//
//    @Test
//    public void test8(){
//        identityService.setAuthenticatedUserId("1");
//        ProcessInstance processInstance = runtimeService.startProcessInstanceById("srm-process:1:1c34f2d5-040a-11ee-be52-00ff1cab5def");
//        System.out.println(processInstance.getName());
//        System.out.println(processInstance.getProcessDefinitionId());
//        System.out.println(processInstance.getStartUserId());
//
//        List<Task> list = taskService.createTaskQuery().active().list();
//        System.out.println(list);
//        list.forEach(System.out::println);
//
//    }
//
//    @Test
//    public void test9(){
//        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().active().list();
//        taskService.setAssignee("4d0797fe-044d-11ee-8d97-00ff1cab5def", "axel");
//        List<Task> list1 = taskService.createTaskQuery().list();
//        list1.forEach(task -> {
//            System.out.println(task.getAssignee());
//        });
//        taskService.complete("4d0797fe-044d-11ee-8d97-00ff1cab5def");
//    }
//
//    @Test
//    public void test10(){
//        List<String> list = repositoryService.getDeploymentResourceNames("1baa17f2-040a-11ee-be52-00ff1cab5def");
//        System.out.println(list);
////        runtimeService.startProcessInstanceByKey("")
//    }
//
//    @Test
//    public void test11(){
////        repositoryService.createDeployment().addClasspathResource("/");
////        repositoryService.suspendProcessDefinitionByKey();
//        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
//        System.out.println(list);
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId("srm-process:1:1c34f2d5-040a-11ee-be52-00ff1cab5def").singleResult();
//        System.out.println(processDefinition);
//        System.out.println(processDefinition.getKey());
//    }
//
//    @Test
//    public void test12(){
//        String taskId = "b07f6ea0-04e4-11ee-8aad-00ff1cab5def";
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        String taskDefinitionKey = task.getTaskDefinitionKey();
//        System.out.println(taskDefinitionKey);
//        String processDefinitionId = task.getProcessDefinitionId();
//        System.out.println(processDefinitionId);
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
//        String key = processDefinition.getKey();
//        System.out.println(key);
//
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
////        List<Process> processes = bpmnModel.getProcesses();
////        System.out.println(processes.size());
////        Process process = processes.get(0);
////        System.out.println(process);
//        Process process = bpmnModel.getProcessById(key);
//        Collection<FlowElement> flowElements = process.getFlowElements();
//        flowElements.forEach(flowElement ->{
//            if(flowElement instanceof StartEvent){
//                System.out.println("流程启动任务！");
//                System.out.println(flowElement.getName());
//                System.out.println(flowElement.getId());
//            }
//            if(flowElement instanceof EndEvent){
//                System.out.println("流程结束任务！");
//                System.out.println(flowElement.getName());
//                System.out.println(flowElement.getId());
//            }
//            if(flowElement instanceof UserTask){
//                System.out.println("用户任务！");
//                System.out.println(flowElement.getName());
//                System.out.println(flowElement.getId());
//            }
//
//        });
//    }
//
//    @Test
//    public void test13(){
////        String taskId = "b07f6ea0-04e4-11ee-8aad-00ff1cab5def";
////        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
////        String taskDefinitionKey = task.getTaskDefinitionKey();
////        String processInstanceId = task.getProcessInstanceId();
////        // 退回到发起者 id： sid-8185742D-5AB2-4D83-A2C3-A73731FE5D80
////        runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId).moveActivityIdTo(taskDefinitionKey, "sid-8185742D-5AB2-4D83-A2C3-A73731FE5D80").changeState();
////        List<Task> list = taskService.createTaskQuery().list();
////        System.out.println(list);
//
//        List<HistoricTaskInstance> list1 = historyService.createHistoricTaskInstanceQuery().finished().list();
//        System.out.println(list1.size());
//        list1.forEach(historicTaskInstance -> {
//            System.out.println(historicTaskInstance.getId());
//            System.out.println(historicTaskInstance.getDurationInMillis()/1000/60/60);
//            System.out.println(historicTaskInstance.getName());
//            System.out.println(historicTaskInstance.getTaskDefinitionKey());
//        });
//    }
//
//    @Test
//    public void test14(){
//        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
//        System.out.println(list);
//        ProcessDefinition processDefinition = list.get(0);
//        List<UserTask> userTasks = WorkflowUtils.listAllUserTaskOfProcessDefinition(processDefinition.getId());
//        System.out.println(userTasks.size());
//        userTasks.forEach(userTask -> {
//            System.out.println("任务节点：");
//            System.out.println(userTask.getName());
//            System.out.println(userTask.getId());
//        });
//        List<SequenceFlow> flows = WorkflowUtils.listAllSequenceFlowOfProcessDefinition(processDefinition.getId());
//        System.out.println(flows.size());
//        flows.forEach(sequenceFlow -> {
//            System.out.println("连接：");
//            System.out.println(sequenceFlow);
//        });
//    }
//
//    @Test
//    public void test15(){
//        List<Task> list = taskService.createTaskQuery().list();
//        Task task = list.get(1);
//        String id = task.getId();
//        FlowElement nextTaskNode = WorkflowUtils.getNextTaskNode(id);
//        FlowElement prevTaskNode = WorkflowUtils.getPrevTaskNode(id);
//        System.out.println(nextTaskNode);
//        System.out.println(prevTaskNode);
//
//        List<String> historicTaskNodeByTaskId = WorkflowUtils.getHistoricTaskNodeByTaskId(id);
//        System.out.println(historicTaskNodeByTaskId);
//        List<UserTask> histoticUserTaskNodeByProcessInstanceId = WorkflowUtils.getHistoricUserTaskNodeByProcessInstanceId(task.getProcessInstanceId());
//        histoticUserTaskNodeByProcessInstanceId.forEach(userTask -> {
//            System.out.println(userTask.getName());
//            System.out.println(userTask.getId());
//            System.out.println(userTask.getAssignee());
//        });
//
//    }
//
//    @Test
//    public void test16(){
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////        BpmnModel bpmnModel = repositoryService.getBpmnModel("srm-process:1:1c34f2d5-040a-11ee-be52-00ff1cab5def");
////        Process mainProcess = bpmnModel.getMainProcess();
//        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
//        System.out.println(list);
//    }
//
//    @Test
//    public void test17(){
////        runtimeService.deleteProcessInstance("39bfd362-0768-11ee-8643-00ff1cab5def", "test");
////        runtimeService.deleteProcessInstance("78b6deb6-044c-11ee-bac1-00ff1cab5def", "test");
////        historyService.deleteHistoricProcessInstance();
//        List<Task> list = taskService.createTaskQuery().listPage(0, 20);
//        System.out.println(list);
////        List<TaskVo> taskVos = workflowTaskService.listPageTask(0, 20);
////        System.out.println(taskVos);
////        List<HistoricProcessInstance> list1 = historyService.createHistoricProcessInstanceQuery().list();
//        List<HistoricProcessInstance> list1 = historyService.createHistoricProcessInstanceQuery().list();
//        List<ProcessInstanceVo> processInstanceVos = ProcessVoUtils.transformFromProcessInstance(list1);
//        System.out.println(processInstanceVos);
//
//        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().finished().list();
//        System.out.println(historicTaskInstances);
//        System.out.println(ProcessVoUtils.transformFromFinishedTask(historicTaskInstances));
//
//
//        List<Task> list2 = taskService.createTaskQuery().list();
//        for (Task task : list2) {
//            taskService.setAssignee(task.getId(), "1");
//        }
//    }
//
////    @Test
////    public void test18(){
////        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/机载产品潜在供应商入册申请流程.bpmn20.xml").name("潜在供应商商入册流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////    }
////
////    @Test
////    public void test19(){
////        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/机载产品获批供应商入册申请流程.bpmn20.xml").name("获批供应商商入册流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////    }
////
////    @Test
////    public void test20(){
////        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/RFI审批流程.bpmn20.xml").name("RFI审批流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////    }
////
////    @Test
////    public void test21(){
////        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/RFP审批流程.bpmn20.xml").name("RFP审批流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////    }
//
////    @Test
////    public void test54(){
////        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/供应商信息变更流程.bpmn20.xml").name("供应商信息变更流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////    }
////
////    @Test
////    public void test55(){
////        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/供应商清退流程.bpmn20.xml").name("供应商清退流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////    }
////
////    @Test
////    public void test56(){
////        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/货源供应商入册流程.bpmn20.xml").name("货源供应商入册流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
////    }
//
//    @Test
//    public void test22(){
//        // 流程定义查询
//        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
//        list.forEach(processDefinition -> {
//            System.out.println(processDefinition.getKey());
//            System.out.println(processDefinition.getName());
//        });
//    }
//
//    @SneakyThrows
//    @Test
//    public void test23() throws IllegalAccessException, InstantiationException {
//        String BASE_PACKAGE = "com.ruoyi";
//        String RESOURCE_PATTERN = "/**/*.class";
//        Map<String, Class> handlerMap = new HashMap<String, Class>();
//        //spring工具类，可以获取指定路径下的全部类
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        try {
//            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
//                    ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + RESOURCE_PATTERN;
//            org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(pattern);
//            //MetadataReader 的工厂类
//            MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
//            for (org.springframework.core.io.Resource resource : resources) {
//                //用于读取类信息
//                MetadataReader reader = readerfactory.getMetadataReader(resource);
//                //扫描到的class
//                String classname = reader.getClassMetadata().getClassName();
//                Class<?> clazz = Class.forName(classname);
//                //判断是否有指定主解
//                ProcessKey annotation = clazz.getAnnotation(ProcessKey.class);
//                if (annotation != null) {
//                    //将注解中的类型值作为key，对应的类作为 value
//                    handlerMap.put(annotation.value(), clazz);
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//        }
//        System.out.println(handlerMap);
//        System.out.println(handlerMap.get("srm-process"));
//        Class<?> aClass = Class.forName(handlerMap.get("srm-process").getName());
//        String typeName = aClass.getTypeName();
//        System.out.println(typeName);
//        Object o = ReflectUtil.newInstance(aClass);
//        Object o1 = JSONObject.parseObject("", handlerMap.get("srm-process"));
//        System.out.println(UserTask.class);
//    }
//
//    @Test
//    public void test30() throws ClassNotFoundException {
//        ProcessStartJsonDto processStartJsonDto = new ProcessStartJsonDto();
//        processStartJsonDto.setProcessKey("srm");
//        processStartJsonDto.setBusinessData("test");
//        String s = JSON.toJSONString(processStartJsonDto);
//        System.out.println(s);
//        Class<ProcessStartJsonDto> processStartJsonDtoClass = ProcessStartJsonDto.class;
//        System.out.println(processStartJsonDtoClass);
//        Class<?> aClass = Class.forName(processStartJsonDtoClass.getTypeName());
//        System.out.println(aClass);
//        System.out.println(processStartJsonDtoClass.getClass());
////        JSONObject.parseObject(s, )
//    }
//
//    @Test
//    public void test33(){
//        String BASE_PACKAGE = "com.ruoyi";
//        String RESOURCE_PATTERN = "/**/*.class";
//        Map<String, Class> handlerMap = new HashMap<String, Class>();
//        //spring工具类，可以获取指定路径下的全部类
//        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//        try {
//            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
//                    ClassUtils.convertClassNameToResourcePath(BASE_PACKAGE) + RESOURCE_PATTERN;
//            org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(pattern);
//            //MetadataReader 的工厂类
//            MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
//            for (org.springframework.core.io.Resource resource : resources) {
//                //用于读取类信息
//                MetadataReader reader = readerfactory.getMetadataReader(resource);
//                //扫描到的class
//                String classname = reader.getClassMetadata().getClassName();
//                Class<?> clazz = Class.forName(classname);
//                //判断是否有指定主解
//                ProcessKey annotation = clazz.getAnnotation(ProcessKey.class);
//                if (annotation != null) {
//                    //将注解中的类型值作为key，对应的类作为 value
//                    handlerMap.put(annotation.value(), clazz);
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//        }
//        System.out.println(handlerMap);
//        System.out.println(handlerMap.get("srm-process"));
////        BusinessAdapterImpl businessAdapter = new BusinessAdapterImpl();
////        ProcessStartJsonDto entity = businessAdapter.createEntity("{\"businessData\":\"test\",\"processKey\":\"srm\"}");
////        System.out.println(entity);
//        String str = "{\"businessData\":\"test\",\"processKey\":\"srm\"}";
//        Class aClass = handlerMap.get("srm-process");
//        String typeName = aClass.getTypeName();
//        Class aClass1 = handlerMap.get("srm-process");
//        String typeName1 = aClass1.getTypeName();
//        Object o = JSONObject.parseObject(str, aClass);
//        System.out.println(o);
//    }
//
//    @Test
//    public void test50(){
//        String str = "{\"businessData\":\"test\",\"processKey\":\"srm-process\"}";
////        BusinessAdapterImpl businessAdapter = new BusinessAdapterImpl();
////        ProcessStartJsonDto entity = businessAdapter.createEntity(str);
//////        System.out.println(entity);
//        ProcessStartJsonDto processStartJsonDto = new ProcessStartJsonDto();
//        processStartJsonDto.setProcessKey("srm-process");
//        processStartJsonDto.setBusinessData(str);
//        workflowIntegrateService.startProcess(processStartJsonDto);
//    }
//
//    @Test
//    public void test51() throws IOException {
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("srm-process").latestVersion().singleResult();
//        String deploymentId = processDefinition.getDeploymentId();
//        InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, processDefinition.getResourceName());
//        String s = IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
//        System.out.println(s);
//
//    }
//
//    @Test
//    public void test52() throws IOException {
//        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId("srm-process:1:1c34f2d5-040a-11ee-be52-00ff1cab5def").singleResult();
//        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
//    }
//
//    @Test
//    public void test53(){
//        String processInstanceId = "ac1e4d9c-0aa6-11ee-a0aa-00ff1cab5def";
//        List<Comment> processInstanceComments = taskService.getProcessInstanceComments(processInstanceId);
//        for (Comment comment : processInstanceComments) {
//            System.out.println(comment.getFullMessage());
//            System.out.println(comment.getTaskId());
//            System.out.println(comment.getType());
//        }
//        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished().list();
//        list.forEach(System.out::println);
//    }
//
//
//}
