package com.example.webDao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chendong
 * @date 2018-08-29
 * @description
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InnerService {

    /**
     * 服务名称
     *
     * @return
     */
    String value() default "";

    //TODO 其他和服务有关的配置待扩展
}
