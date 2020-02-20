package com.example.bspringboot.e_ioc;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author chenpiqian
 * @date: 2020-02-20
 */
@Component
public class MyPostConstruct {

/**
 * bean初始化完成，依赖注入完成 后调用

 init:19, MyPostConstruct (com.example.bspringboot.e_ioc)
 invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
 invoke:62, NativeMethodAccessorImpl (sun.reflect)
 invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
 invoke:498, Method (java.lang.reflect)
 invoke:389, InitDestroyAnnotationBeanPostProcessor$LifecycleElement (org.springframework.beans.factory.annotation)
 invokeInitMethods:333, InitDestroyAnnotationBeanPostProcessor$LifecycleMetadata (org.springframework.beans.factory.annotation)
 postProcessBeforeInitialization:157, InitDestroyAnnotationBeanPostProcessor (org.springframework.beans.factory.annotation)
 applyBeanPostProcessorsBeforeInitialization:416, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 initializeBean:1788, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 doCreateBean:595, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 createBean:517, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 lambda$doGetBean$0:323, AbstractBeanFactory (org.springframework.beans.factory.support)
 getObject:-1, 1757317128 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$167)
 getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
 doGetBean:321, AbstractBeanFactory (org.springframework.beans.factory.support)
 getBean:202, AbstractBeanFactory (org.springframework.beans.factory.support)
 preInstantiateSingletons:879, DefaultListableBeanFactory (org.springframework.beans.factory.support)
 finishBeanFactoryInitialization:878, AbstractApplicationContext (org.springframework.context.support)
 refresh:550, AbstractApplicationContext (org.springframework.context.support)
 refresh:141, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
 refresh:747, SpringApplication (org.springframework.boot)
 refreshContext:397, SpringApplication (org.springframework.boot)
 run:315, SpringApplication (org.springframework.boot)
 run:1226, SpringApplication (org.springframework.boot)
 run:1215, SpringApplication (org.springframework.boot)
 main:13, BSpringBootApplication (com.example.bspringboot)
 */
    @PostConstruct
    public void init(){
        System.out.println("################init##########");
    }
}
