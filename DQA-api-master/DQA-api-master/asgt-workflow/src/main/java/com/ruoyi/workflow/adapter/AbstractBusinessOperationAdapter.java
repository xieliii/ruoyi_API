package com.ruoyi.workflow.adapter;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.ruoyi.workflow.domain.AgWorkflowAssigneeEntity;
import com.ruoyi.workflow.model.dto.ProcessStartAdapterDto;
import org.flowable.bpmn.model.UserTask;

import java.util.List;

/**
 * Created by Axel on 2023/6/7 11:45
 *  用于操作业务数据
 * @author Axel
 */
public abstract class AbstractBusinessOperationAdapter<T> {
    /**
     * 流程业务适配器，用于保存业务数据
     * @param adapterDto 业务数据
     * @return 保存后的业务数据，返回的数据中必须包含id,
     *         用于唯一标识业务数据，类似于表单id
     */
    public abstract T saveBusinessData(ProcessStartAdapterDto<T> adapterDto);

    /**
     * 更新业务数据接口
     * @param t 业务数据
     * @return 更新后的业务数据
     */
    public abstract T updateBusinessData(T t);

    /**
     * 根据业务id获取流程业务数据
     * @param id 业务id
     * @return 业务数据
     */
    public abstract T getBusinessData(String id);

    /**
     * 根据业务id删除业务数据
     * @param id 业务id
     */
    public abstract void deleteBusinessData(String id);

    /**
     * 插入流程处理人信息
     * @param list 流程处理人信息
     * @return 流程处理人数据
     */
    public abstract List<AgWorkflowAssigneeEntity> saveWorkflowAssignee(List<UserTask> list);


    /**
     * 解析字符串到实体，已办使用固定模板写法
     * @param jsonStr 字符串标识
     * @return 业务数据类型
     */
    public T createEntity(String jsonStr){
        Assert.notEmpty(jsonStr, "当前业务数据为空，无法进行流程操作！");
        Class<T> superClassGenericType = (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), 0);
        return JSONObject.parseObject(jsonStr, superClassGenericType);
    }

}
