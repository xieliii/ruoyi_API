package com.ruoyi.workflow.listener;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

/**
 * Created by Axel on 2023/6/1 9:43
 *
 * @author Axel
 */
@Slf4j
@Component
public class FlowTaskListener implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println(delegateTask.getAssignee());
        System.out.println(delegateTask.getEventName());
    }
}
