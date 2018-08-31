package com.example.webDao.annotation;


import com.example.webDao.bind.WebDaoProxyFactory;
import com.example.webDao.register.WebDaoScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author chendong
 * @date 2018-08-30
 * @description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(WebDaoScannerRegistrar.class)
public @interface WebDaoScan {

    /**
     * 需要扫描的位置的数组
     *
     * @return
     */
    String[] value() default {};

    /**
     * 需要扫描的位置
     *
     * @return
     */
    String[] basePackages() default {};

    /**
     * 设置需要扫描的接口的注解
     *
     * @return
     */
    Class<? extends Annotation> annotationClass() default Annotation.class;

    /**
     * 需要被代理的工厂类
     *
     * @return
     */
    Class<? extends WebDaoProxyFactory> factoryBean() default WebDaoProxyFactory.class;

    /**
     * 设置默认的请求方法
     *
     * @return
     */
    Class<? extends Annotation> defaultMethod() default Params.class;

}
