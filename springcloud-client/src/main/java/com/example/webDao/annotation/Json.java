package com.example.webDao.annotation;

import java.lang.annotation.*;

/**
 * @author chendong
 * @date 2018-08-31
 * @description
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Json {
    /**
     * 访问路径
     *
     * @return
     */
    String value();
}
