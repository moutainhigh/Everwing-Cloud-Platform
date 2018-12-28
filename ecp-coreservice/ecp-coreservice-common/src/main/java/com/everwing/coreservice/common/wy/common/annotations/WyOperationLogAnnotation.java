package com.everwing.coreservice.common.wy.common.annotations;

/**
 * Created by wust on 2017/6/2.
 */

import com.everwing.coreservice.common.wy.common.enums.OperationEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WyOperationLogAnnotation {
    // 模块名
    OperationEnum moduleName();

    // 业务名称
    String businessName();

    // 操作类型
    OperationEnum operationType();
}
