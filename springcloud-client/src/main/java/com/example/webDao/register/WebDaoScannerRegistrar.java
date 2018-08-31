package com.example.webDao.register;

import com.example.webDao.annotation.WebDaoScan;
import com.example.webDao.bind.WebDaoProxyFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chendong
 * @date 2018-08-30
 * @description
 */

public class WebDaoScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private ResourceLoader resourceLoader;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(WebDaoScan.class.getName()));
        ClassPathWebDaoScanner scanner = new ClassPathWebDaoScanner(registry);
        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        //注册要扫描的注解类型，可以自定义，如果没有就使用默认的
        Class<? extends Annotation> annotationClass = annoAttrs.getClass("annotationClass");
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        //注册要使用的默认请求方法的注解类型，可以自定义，如果没有就使用默认的。
        Class<? extends Annotation> defaultMethod = annoAttrs.getClass("defaultMethod");
        if (!Annotation.class.equals(defaultMethod)) {
            scanner.setDefaultMethod(defaultMethod);
        }


        List<String> basePackages = new ArrayList<String>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : annoAttrs.getStringArray("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        Class<? extends WebDaoProxyFactory> mapperFactoryBeanClass = annoAttrs.getClass("factoryBean");
        if (!WebDaoProxyFactory.class.equals(mapperFactoryBeanClass)) {
            scanner.setMyFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
        }

        scanner.registerFilters();

        scanner.doScan(StringUtils.toStringArray(basePackages));
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
