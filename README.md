# webDao

模仿Spring cloud Feign的形式，按照接口+注解的方式，进行Http调。 
在项目用springCloud技术栈重构时开，在调用的规则上，进行了一些优化。

## 1.主要思路：
     按照Mybatis与spring整合的方法，在spring加载bean进入容器时，通过扫描指定包下的被指定注解修饰的接口，
     在spring 创建 Beandifine 的时候，使用代理工厂创建实际的动态代理类。



## 2.执行步骤：
     当调用接口的方法时，实际会被动态代理，在动态代理的内部，对接口进行关键参数的解析，然后拼装目标url和
     获取请求参数,再通过RestTemplate进行ribbon负载均衡调用其他的微服务。



## 3.使用方法
     3.1.在启动类加上注解@WebDaoScan,添加相关参数：需要扫描的包，需要扫描的接口的指定注解，其他的均有默认值
     3.2.在接口上增加指定的注解，在方法上选择使用@Json 或者 @Params 注解，表示使用请求的方式。
    
     
