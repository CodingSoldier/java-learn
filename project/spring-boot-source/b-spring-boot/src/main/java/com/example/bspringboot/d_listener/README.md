ApplicationListener 应用程序事件监听器

@FunctionalInterface 注解的类只能有一个抽象方法

ApplicationEventMulticaster 应用程序广播器，管理监听器并广播Event

事件发送顺序：
    框架启动、starting、environmentPrepared、contextInitialized、prepared、started、ready、启动完毕
    
监听器注册的源码跟系统初始化器几乎是一样的，源码：
org.springframework.boot.SpringApplication.SpringApplication()
    setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
    setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
        都是从spring.factories中读取配置，然后通过SpringFactoriesLoader加载接口的实现类
        最后将实现类集合设置给SpringApplication的listeners属性
    
org.springframework.boot.context.event.EventPublishingRunListener.starting
    与d_event_2.WeatherRunListener类似，spring同样对事件广播进行了封装
       
    
    
    
    
    
    
    
    
    
    