package com.example.webDao.bind;

import com.example.webDao.annotation.InnerService;
import com.example.webDao.annotation.Json;
import com.example.webDao.annotation.Params;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chendong
 */
public class WebDaoProxy implements InvocationHandler {

    private Class<?> interfaceClass;

    private Class<? extends Annotation> defaultMethod;

    private Class<? extends Annotation> annotationClass;

    private String Json_key = "Json";
    private String Params_key = "Params";
    private String Default_key = "default";

    public Object bind(Class<?> cls, Class<? extends Annotation> del, Class<? extends Annotation> an) {
        this.interfaceClass = cls;
        this.defaultMethod = del;
        this.annotationClass = an;
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{interfaceClass}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Annotation classAnnotation = interfaceClass.getAnnotation(this.annotationClass);
        InnerService innerService = (InnerService) classAnnotation;
        String serviceName = innerService.value();
        if (serviceName == null) {
            throw new IllegalArgumentException("Service name can not be null,please check your argument");
        }
        //获取方法名
        String methodName = method.getName();
        //获取请求参数，限定请求参数的类型
        if (args.length > 1) {
            throw new IllegalArgumentException("The default argument is a single param which type is Map ,please check your argument");
        } else if (args.length < 1) {
            args[0] = new HashMap<>();
        } else if (!(args[0] instanceof Map)) {
            throw new IllegalArgumentException("The default argument is a single param which type is Map ,please check your argument");
        }
        //获取方法上的注解
        Annotation[] annotations = method.getDeclaredAnnotations();
        HashMap<Object, Annotation> map = new HashMap<>();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Json.class)) {
                map.put(Json_key, annotation);
            }
            if (annotation.annotationType().equals(Params.class)) {
                map.put(Params_key, annotation);
            }
        }
        if (map.size() > 1) {
            throw new IllegalArgumentException("one Method only have a kind of Annotation which is Json or Params，exist 2:" +
                    map.get(Json_key).annotationType() + "  ,  " + map.get(Params_key).annotationType());
        }
        if (map.isEmpty()) {
            if (defaultMethod == null ||
                    (!defaultMethod.equals(Json.class) && !defaultMethod.equals(Params.class))) {
                throw new IllegalArgumentException("The default http request method annotation can not be Null or disable Type" + defaultMethod);
            }
            //设置默认的调用方法：
            map.put(Default_key, defaultMethod.newInstance());
        }

        Annotation annotation = map.get(Json_key) == null ? map.get(Params_key) == null ? map.get(Default_key) : map.get(Params_key) : map.get(Json_key);
        return processAskMethodAndValue(annotation, methodName, (Map) args[0], serviceName);

    }

    /**
     * 包装求情方法和请求的值
     *
     * @param annotation
     * @param methodName
     * @param map
     * @param serviceName
     * @return
     */
    private Object processAskMethodAndValue(Annotation annotation, String methodName, Map map, String serviceName) {
        String url = "";
        if (annotation.annotationType().equals(Json.class)) {
            Json json = (Json) annotation;
            String path = json.value();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 2);
            }
            url = "http://" + serviceName + "/" + path + "/" + methodName;

        } else if (annotation.annotationType().equals(Params.class)) {
            Params params = (Params) annotation;
            String path = params.value();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            url = "http://" + serviceName + "/" + path + "/" + methodName;
        }

        return url;
    }

}