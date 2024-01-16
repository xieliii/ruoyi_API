package com.ruoyi.workflow.config;

import com.ruoyi.workflow.adapter.AbstractBusinessOperationAdapter;
import com.ruoyi.workflow.annotaions.ProcessKey;
import com.ruoyi.workflow.listener.BusinessWorkflowLifeCycleListener;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Axel on 2023/6/7 17:11
 *
 * @author Axel
 */
@Component
public class BusinessWorkflowConfig implements InitializingBean, ApplicationContextAware {
    private Map<String, BusinessWorkflowLifeCycleListener> map = new HashMap<>();
    private Map<String, AbstractBusinessOperationAdapter> operationMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, BusinessWorkflowLifeCycleListener> beansOfType = applicationContext.getBeansOfType(BusinessWorkflowLifeCycleListener.class);
        for (BusinessWorkflowLifeCycleListener listener : beansOfType.values()) {
            boolean aopProxy = AopUtils.isAopProxy(listener);
            if(aopProxy){
                Class<?> targetClass = AopUtils.getTargetClass(listener);
                boolean annotationPresent1 = targetClass.isAnnotationPresent(ProcessKey.class);
                if(annotationPresent1){
                    this.map.put(targetClass.getAnnotation(ProcessKey.class).value(), listener);
                }
            }else{
                boolean annotationPresent1 = listener.getClass().isAnnotationPresent(ProcessKey.class);
                if(annotationPresent1){
                    this.map.put(listener.getClass().getAnnotation(ProcessKey.class).value(), listener);
                }
            }
        }
        Map<String, AbstractBusinessOperationAdapter> operationAdapterMap = applicationContext.getBeansOfType(AbstractBusinessOperationAdapter.class);
        for (AbstractBusinessOperationAdapter value : operationAdapterMap.values()) {
            boolean aopProxy = AopUtils.isAopProxy(value);
            if(aopProxy){
                Class<?> targetClass = AopUtils.getTargetClass(value);
                boolean annotationPresent1 = targetClass.isAnnotationPresent(ProcessKey.class);
                if(annotationPresent1){
                    this.operationMap.put(targetClass.getAnnotation(ProcessKey.class).value(), value);
                }
            }else{
                boolean annotationPresent1 = value.getClass().isAnnotationPresent(ProcessKey.class);
                if(annotationPresent1){
                    this.operationMap.put(value.getClass().getAnnotation(ProcessKey.class).value(), value);
                }
            }

        }
    }

    public BusinessWorkflowLifeCycleListener createBusinessWorkflowService(String businessKey){
        BusinessWorkflowLifeCycleListener businessWorkflowService = this.map.get(businessKey);
        if(ObjectUtils.isEmpty(businessWorkflowService)){
            throw new RuntimeException("未找到"+ businessKey +"流程监听器业务实现，无法完成业务流程操作！");
        }
        return businessWorkflowService;
    }

    public AbstractBusinessOperationAdapter createBusinessOperationAdapter(String businessKey){
        AbstractBusinessOperationAdapter businessOperationAdapter = this.operationMap.get(businessKey);
        if(ObjectUtils.isEmpty(businessOperationAdapter)){
            throw new RuntimeException("未找到"+ businessKey +"流程操作业务实现，无法完成业务流程操作！");
        }
        return businessOperationAdapter;
    }
}
