package com.ruoyi.workflow.config;

import com.ruoyi.workflow.listener.GlobalEventListener;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Axel on 2023/6/5 19:18
 *
 * @author Axel
 */
@Slf4j
@Component
@Configuration
public class WorkflowConfig implements CommandLineRunner {
    @Resource
    private RuntimeService runtimeService;

    @Resource
    private GlobalEventListener globalEventListener;

    @Override
    public void run(String... args) throws Exception {
        runtimeService.addEventListener(globalEventListener);
    }
}
