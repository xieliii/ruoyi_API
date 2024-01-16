package com.ruoyi.workflow.utils;

import cn.hutool.core.lang.Assert;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.workflow.model.vo.ProcessStatisticVo;
import org.flowable.engine.HistoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Axel on 2023/6/26 13:48
 *
 * @author Axel
 */
public class ProcessHistoryStatisticTask extends RecursiveTask<ProcessStatisticVo> {
    private SecurityContext context;
    private ProcessDefinition processDefinition;

    public ProcessHistoryStatisticTask(SecurityContext context, ProcessDefinition processDefinition) {
        this.context = context;
        this.processDefinition = processDefinition;
    }

    @Override
    protected ProcessStatisticVo compute() {
        SecurityContextHolder.setContext(context);
        Assert.notNull(processDefinition, "流程定义为空，无法进行统计信息获取!");
        HistoryService historyService = SpringUtils.getBean(HistoryService.class);
        String key = processDefinition.getKey();
        String processDefinitionId = processDefinition.getId();
        String name = processDefinition.getName();
        long count = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).finished().count();
        long count1 = historyService.createHistoricProcessInstanceQuery().processDefinitionId(processDefinitionId).unfinished().count();
        ProcessStatisticVo processStatisticVo = new ProcessStatisticVo();
        processStatisticVo.setProcessKey(key);
        processStatisticVo.setProcessName(name);
        processStatisticVo.setFinishedCount(count);
        processStatisticVo.setRunningCount(count1);
        return processStatisticVo;
    }
}
