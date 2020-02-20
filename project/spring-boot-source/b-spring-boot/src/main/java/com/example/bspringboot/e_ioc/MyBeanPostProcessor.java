//package com.example.bspringboot.e_ioc;
//
//import com.example.bspringboot.e_ioc.bean.GetBeanTest;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
//import org.springframework.stereotype.Component;
//
//
///**
// * 一般不会通过实现InstantiationAwareBeanPostProcessor来注入修改bean
// */
//@Component
//public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
//
//    /**
//     *
//     postProcessBeforeInstantiation:12, MyBeanPostProcessor (com.example.bspringboot.e_ioc)
//     applyBeanPostProcessorsBeforeInstantiation:1141, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     resolveBeforeInstantiation:1114, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     createBean:506, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     lambda$doGetBean$0:323, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getObject:-1, 69062746 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$171)
//     getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
//     doGetBean:321, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getBean:207, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getWebServerFactory:210, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     createWebServer:179, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     onRefresh:153, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     refresh:544, AbstractApplicationContext (org.springframework.context.support)
//     refresh:141, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     refresh:747, SpringApplication (org.springframework.boot)
//     refreshContext:397, SpringApplication (org.springframework.boot)
//     run:315, SpringApplication (org.springframework.boot)
//     run:1226, SpringApplication (org.springframework.boot)
//     run:1215, SpringApplication (org.springframework.boot)
//     main:13, BSpringBootApplication (com.example.bspringboot)
//     */
//    @Override
//    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
//        return beanName.equals("getBeanTest") ? new GetBeanTest() : null;
//    }
//
//    /**
//     postProcessAfterInstantiation:41, MyBeanPostProcessor (com.example.bspringboot.e_ioc)
//     populateBean:1388, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     doCreateBean:594, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     createBean:517, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     lambda$doGetBean$0:323, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getObject:-1, 1404932042 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$143)
//     getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
//     doGetBean:321, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getBean:202, AbstractBeanFactory (org.springframework.beans.factory.support)
//     instantiateUsingFactoryMethod:409, ConstructorResolver (org.springframework.beans.factory.support)
//     instantiateUsingFactoryMethod:1338, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     createBeanInstance:1177, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     doCreateBean:557, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     createBean:517, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//     lambda$doGetBean$0:323, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getObject:-1, 1404932042 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$143)
//     getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
//     doGetBean:321, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getBean:207, AbstractBeanFactory (org.springframework.beans.factory.support)
//     getWebServerFactory:210, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     createWebServer:179, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     onRefresh:153, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     refresh:544, AbstractApplicationContext (org.springframework.context.support)
//     refresh:141, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
//     refresh:747, SpringApplication (org.springframework.boot)
//     refreshContext:397, SpringApplication (org.springframework.boot)
//     run:315, SpringApplication (org.springframework.boot)
//     run:1226, SpringApplication (org.springframework.boot)
//     run:1215, SpringApplication (org.springframework.boot)
//     main:13, BSpringBootApplication (com.example.bspringboot)
//     */
//    @Override
//    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
//        if (beanName.equals("getBeanTest")) {
//            GetBeanTest getBeanTest = (GetBeanTest) bean;
//            getBeanTest.setName("wangwu");
//        }
//        return false;
//    }
//}
