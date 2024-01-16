//package com.ruoyi;
//
//import cn.xuyanwu.spring.file.storage.FileStorageService;
//import com.ruoyi.workflow.service.WorkflowIntegrateService;
//import com.ruoyi.workflow.service.WorkflowTaskService;
//import org.flowable.engine.*;
//import org.flowable.engine.history.HistoricActivityInstance;
//import org.flowable.engine.repository.ProcessDefinition;
//import org.flowable.task.api.history.HistoricTaskInstance;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.SpringApplicationRunListener;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Axel on 2023/6/21 13:41
// *
// * @author Axel
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class WorkflowTest2 {
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
//    @Test
//    public void test(){
//        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().processInstanceId("abba4dad-0f5e-11ee-bb12-a4bb6dd41932").finished().list();
//        list.forEach(System.out::println);
//        System.out.println("888888888888888888888");
//        List<HistoricActivityInstance> list2 = historyService.createHistoricActivityInstanceQuery().processInstanceId("abba4dad-0f5e-11ee-bb12-a4bb6dd41932").unfinished().list();
//        list2.forEach(System.out::println);
////        List<HistoricTaskInstance> list1 = historyService.createHistoricTaskInstanceQuery().processInstanceId("12b0ce94-0e7a-11ee-8d92-00ff1cab5def").list();
////        list1.forEach(System.out::println);
//    }
//
//    @Test
//    public void test2(){
//        // 流程部署
////        repositoryService.createDeployment().addClasspathResource("process/RFQ审批流程.bpmn20.xml").name("RFQ审批流程").category("srm").deploy();
////        repositoryService.createDeployment().addClasspathResource("process/RFP审批流程.bpmn20.xml").name("RFP审批流程").category("srm").deploy();
////        repositoryService.createDeployment().addClasspathResource("process/货源供应商入册流程.bpmn20.xml").name("货源供应商入册流程").category("srm").deploy();
////        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
////        System.out.println(list);
//
//
//    }
//
//    @Test
//    public void test3(){
//        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().list();
//        System.out.println(list.size());
//        List<HistoricTaskInstance> list1 = historyService.createHistoricTaskInstanceQuery().finished().list();
//        System.out.println(list1.size());
//        list1.forEach(historicTaskInstance -> System.out.println(historicTaskInstance.getEndTime()));
//        List<HistoricTaskInstance> list2 = historyService.createHistoricTaskInstanceQuery().unfinished().list();
//        System.out.println(list2.size());
//        list2.forEach(historicTaskInstance -> System.out.println(historicTaskInstance.getEndTime()));
//    }
//
//    @Test
//    public void test4(){
////        repositoryService.deleteDeployment("b06a6712-1008-11ee-92b5-00ff1cab5def", true);
////        repositoryService.deleteDeployment("922c1866-1008-11ee-8e94-00ff1cab5def", true);
////        repositoryService.deleteDeployment("06a8a664-0e81-11ee-80ba-00ff1cab5def", true);
////        repositoryService.deleteDeployment("eebbe484-08e8-11ee-982a-00ff1cab5def", true);
//    }
//}
