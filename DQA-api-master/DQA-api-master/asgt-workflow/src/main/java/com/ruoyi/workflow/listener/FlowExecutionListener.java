package com.ruoyi.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

/**
 * Created by Axel on 2023/6/1 9:45
 *
 * @author Axel
 */
@Slf4j
@Component
public class FlowExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) {
        System.out.println(delegateExecution.getEventName());
        System.out.println(delegateExecution.getProcessDefinitionId());
        System.out.println(delegateExecution.getCurrentActivityId());
    }
}
