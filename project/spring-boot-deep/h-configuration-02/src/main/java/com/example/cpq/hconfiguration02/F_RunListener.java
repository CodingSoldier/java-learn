package com.example.cpq.hconfiguration02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

public class F_RunListener implements SpringApplicationRunListener, Ordered {

    public static Integer ORDER = -1;
    //public static Integer ORDER = 1;

    /**
     SpringApplication run方法监听，需要在spring.factories中配置，spring.factories中的实现类都是通过SpringFactoriesLoader加载
     SpringApplicationRunListener有说明，实现类必须声明一个构造器，构造器接收application实例和args参数
     构造器调用栈
         <init>:23, F_RunListener (com.example.cpq.hconfiguration02)
         ........
         createSpringFactoriesInstances:444, SpringApplication (org.springframework.boot)
         getSpringFactoriesInstances:427, SpringApplication (org.springframework.boot)
         getRunListeners:413, SpringApplication (org.springframework.boot)
         run:312, SpringApplication (org.springframework.boot)
         run:137, SpringApplicationBuilder (org.springframework.boot.builder)
         main:16, F_Main (com.example.cpq.hconfiguration02)
     */
    public F_RunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {

    }

    /**
     * 一旦环境准备好了，在创建ApplicationContext之前调用
     * @param environment
     */
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        System.out.println("################F_RunListener#environmentPrepared");
        System.out.println("################F_RunListener#ORDER "+ORDER);
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Object> source = new HashMap<>(16);
        source.put("user.city.name", "F_RunListener.environmentPrepared");
        MapPropertySource environmentPrepared = new MapPropertySource("environmentPrepared", source);
        propertySources.addFirst(environmentPrepared);
    }

    /**
     * 比environmentPrepared更加靠后执行，
     * 也可以通过监听ApplicationPreparedEvent来实现
     * 优先使用事件监听来实现，修改environment对象最好在refreshContext执行前进行
     */
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("################F_RunListener#contextPrepared");
        System.out.println("################F_RunListener#ORDER "+ORDER);
        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
        Map<String, Object> source = new HashMap<>(16);
        source.put("user.city.name", "F_RunListener.contextPrepared");
        MapPropertySource environmentPrepared = new MapPropertySource("contextPrepared", source);
        propertySources.addFirst(environmentPrepared);
    }

    /**
     * 比contextPrepared、environmentPrepared更加靠后执行
     */
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("################F_RunListener#contextLoaded");
        System.out.println("################F_RunListener#ORDER "+ORDER);
        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
        Map<String, Object> source = new HashMap<>(16);
        source.put("user.city.name", "F_RunListener.contextLoaded");
        MapPropertySource environmentPrepared = new MapPropertySource("contextLoaded", source);
        propertySources.addFirst(environmentPrepared);
    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    @Override
    public int getOrder() {
        return ORDER;
    }
}
