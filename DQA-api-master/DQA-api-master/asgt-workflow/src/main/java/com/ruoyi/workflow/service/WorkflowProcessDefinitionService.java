package com.ruoyi.workflow.service;

/**
 * Created by Axel on 2023/6/19 14:58
 *
 * @author Axel
 */
public interface WorkflowProcessDefinitionService {
    /**
     * 根据流程键获取最新流程xml
     * @param key 流程键
     * @return xml数据
     */
    String getXmlDataByKey(String key);
}
