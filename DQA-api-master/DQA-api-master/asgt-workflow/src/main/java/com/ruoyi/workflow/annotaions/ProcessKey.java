package com.ruoyi.workflow.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Axel on 2023/6/7 17:09
 * 业务流程Key，唯一标记业务类型
 * @author Axel
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProcessKey {
    String value();
}
