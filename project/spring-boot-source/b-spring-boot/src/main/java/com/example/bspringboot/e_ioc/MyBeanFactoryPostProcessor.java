package com.example.bspringboot.e_ioc;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author chenpiqian
 * @date: 2020-02-20
 */
@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
/**
 * bean加载后修改bean的属性，甚至重新初始化bean

 源码调用栈：
 postProcessBeanFactory:113, MyBeanFactoryPostProcessor (com.example.bspringboot.e_ioc)
 invokeBeanFactoryPostProcessors:286, PostProcessorRegistrationDelegate (org.springframework.context.support)
 invokeBeanFactoryPostProcessors:181, PostProcessorRegistrationDelegate (org.springframework.context.support)
 invokeBeanFactoryPostProcessors:706, AbstractApplicationContext (org.springframework.context.support)
 refresh:532, AbstractApplicationContext (org.springframework.context.support)
 refresh:141, ServletWebServerApplicationContext (org.springframework.boot.web.servlet.context)
 refresh:747, SpringApplication (org.springframework.boot)
 refreshContext:397, SpringApplication (org.springframework.boot)
 run:315, SpringApplication (org.springframework.boot)
 run:1226, SpringApplication (org.springframework.boot)
 run:1215, SpringApplication (org.springframework.boot)
 main:13, BSpringBootApplication (com.example.bspringboot)
 */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition teacher = beanFactory.getBeanDefinition("teacher");
        MutablePropertyValues propertyValues = teacher.getPropertyValues();
        propertyValues.addPropertyValue("name", "wangwu");
    }
}
