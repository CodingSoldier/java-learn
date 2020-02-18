package com.example.bspringboot.d_listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * 监听ApplicationStartedEvent
 * 在spring.factories中配置监听接口实现类

 监听器注册阶段：
 监听器注册的源码跟系统初始化器几乎是一样的，源码：
 org.springframework.boot.SpringApplication.SpringApplication()
     setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
     setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
     都是从spring.factories中读取配置，然后通过SpringFactoriesLoader加载接口的实现类，将实现类集合设置给SpringApplication的listeners属性

 org.springframework.boot.context.event.EventPublishingRunListener.starting
     与d_event_2.WeatherRunListener类似，spring同样对事件广播进行了封装
     使得监听的内部实现与外部调用隔离开了，外包并不会直接调用监听类，而是调用EventPublishingRunListener封装的方法，比方说调用EventPublishingRunListener就不会涉及new Event()

*/
@Order(1)
public class A_Listener implements ApplicationListener<ApplicationStartedEvent> {
/**
 监听器执行阶段：
 onApplicationEvent:15, A_Listener (com.example.bspringboot.d_listener)
 onApplicationEvent:11, A_Listener (com.example.bspringboot.d_listener)
 doInvokeListener:172, SimpleApplicationEventMulticaster (org.springframework.context.event)
 invokeListener:165, SimpleApplicationEventMulticaster (org.springframework.context.event)
 multicastEvent:139, SimpleApplicationEventMulticaster (org.springframework.context.event)
     # ApplicationListener<?> listener : getApplicationListeners(event, type) 获取type类型even的监听器列表
        # retrieveApplicationListeners(eventType, sourceType, retriever); 返回监听了此even的监听器
           # doInvokeListener:172, SimpleApplicationEventMulticaster (org.springframework.context.event)
              # invokeListener:165, SimpleApplicationEventMulticaster (org.springframework.context.event)
                 # listener.onApplicationEvent(event);调用实现类的onApplicationEvent()
 publishEvent:403, AbstractApplicationContext (org.springframework.context.support)
 publishEvent:360, AbstractApplicationContext (org.springframework.context.support)
 started:98, EventPublishingRunListener (org.springframework.boot.context.event)
 started:71, SpringApplicationRunListeners (org.springframework.boot)
 run:321, SpringApplication (org.springframework.boot)
 main:24, BSpringBootApplication (com.example.bspringboot)

 */
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("####### A_Listener##ApplicationStartedEvent");
    }
}
