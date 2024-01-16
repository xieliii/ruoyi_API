package com.ruoyi.workflow.factory;

import lombok.Getter;
import org.flowable.engine.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Axel on 2023/6/1 9:42
 *
 * @author Axel
 */
@Component
@Getter
public class FlowServiceFactory {
    @Resource
    protected RepositoryService repositoryService;

    @Resource
    protected RuntimeService runtimeService;

    @Resource
    protected IdentityService identityService;

    @Resource
    protected TaskService taskService;

    @Resource
    protected HistoryService historyService;

    @Resource
    protected ManagementService managementService;

    @Qualifier("processEngine")
    @Resource
    protected ProcessEngine processEngine;
}
