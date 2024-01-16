package com.ruoyi.workflow.service.impl;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.workflow.factory.FlowServiceFactory;
import com.ruoyi.workflow.service.WorkflowProcessDefinitionService;
import com.ruoyi.workflow.utils.WorkflowUtils;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Service;

/**
 * Created by Axel on 2023/6/19 14:59
 *
 * @author Axel
 */
@Service
public class WorkflowProcessDefinitionServiceImpl extends FlowServiceFactory implements WorkflowProcessDefinitionService {
    @Override
    public String getXmlDataByKey(String key) {
        if(StringUtils.isBlank(key)){
            throw new RuntimeException("当前流程键为空，无法进行流程定义数据查询！");
        }
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key).latestVersion().singleResult();
        Assert.notNull(processDefinition, "当前流程定义不存在，无法获取正确的流程Xml数据，请刷新页面后重试！");
        String processDefinitionId = processDefinition.getId();
        return WorkflowUtils.getXmlData(processDefinitionId);
    }
}
