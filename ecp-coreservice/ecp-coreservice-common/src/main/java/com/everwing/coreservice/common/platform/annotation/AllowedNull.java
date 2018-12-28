package com.everwing.coreservice.common.platform.annotation;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface AllowedNull {

    public boolean value() default true;
}
