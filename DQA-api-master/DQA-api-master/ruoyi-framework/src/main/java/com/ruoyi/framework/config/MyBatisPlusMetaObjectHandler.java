package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * Created by Axel on 2023/5/26 10:03
 *
 * @author Axel
 * mybatis-plus插入和更新策略配置
 */
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("insert fill");
        Object deleteFlag = this.getFieldValByName("deleteFlag", metaObject);
        Object creatorUserId = this.getFieldValByName("creatorUserId", metaObject);
        Object creatorTime = this.getFieldValByName("creatorTime", metaObject);
        Object creatorUser = this.getFieldValByName("creatorUser", metaObject);

        if(ObjectUtils.isEmpty(deleteFlag)){
            this.setFieldValByName("deleteFlag", 0, metaObject);
        }
        if(ObjectUtils.isEmpty(creatorUserId)){
            this.setFieldValByName("creatorUserId", String.valueOf(SecurityUtils.getUserId()), metaObject);
        }
        if(ObjectUtils.isEmpty(creatorTime)){
            this.setFieldValByName("creatorTime", DateUtils.getNowDate(), metaObject);
        }
        if(ObjectUtils.isEmpty(creatorUser)){
            this.setFieldValByName("creatorUser", SecurityUtils.getUsername(), metaObject);
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("lastModifyTime", DateUtils.getNowDate(), metaObject);
        this.setFieldValByName("lastModifyUserId", String.valueOf(SecurityUtils.getUserId()), metaObject);
        this.setFieldValByName("lastModifyUser", SecurityUtils.getUsername(), metaObject);
    }
}
