package com.example.bspringboot.h_properties;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 实现EnvironmentAware来获取环境配置属性，但本类还是要加入到spring容器中
 */
@Component
public class MyEnvironmentAware implements EnvironmentAware {

    private static Environment env;

    public static String getPropertyKey(){
        String pk = env.getProperty("property.key");
        System.out.println(pk);
        return pk;
    }

/**
 setEnvironment:45, MyEnvironmentAware (com.example.bspringboot.h_properties)
 invokeAwareInterfaces:108, ApplicationContextAwareProcessor (org.springframework.context.support)
 postProcessBeforeInitialization:100, ApplicationContextAwareProcessor (org.springframework.context.support)
 applyBeanPostProcessorsBeforeInitialization:416, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 initializeBean:1788, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 doCreateBean:595, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 createBean:517, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
 lambda$doGetBean$0:323, AbstractBeanFactory (org.springframework.beans.factory.support)
 getObject:-1, 1780399359 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$174)
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
 main:39, BSpringBootApplication (com.example.bspringboot)
 */
    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
    }

}
