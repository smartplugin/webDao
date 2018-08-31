package com.example.webDao.bind;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.annotation.Annotation;

/**
 * @param <T>
 * @author chendong
 */
public class WebDaoProxyFactory<T> implements InitializingBean, FactoryBean<T> {

    private Class<T> interfaceClass;

    private Class<? extends Annotation> defaultMethod;

    private Class<? extends Annotation> annotationClass;

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Class<T> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<? extends Annotation> getDefaultMethod() {
        return defaultMethod;
    }

    public void setDefaultMethod(Class<? extends Annotation> defaultMethod) {
        this.defaultMethod = defaultMethod;
    }

    @Override
    public T getObject() throws Exception {
        return (T) new WebDaoProxy().bind(interfaceClass, defaultMethod, annotationClass);
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    @Override
    public boolean isSingleton() {
        // 单例模式
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}