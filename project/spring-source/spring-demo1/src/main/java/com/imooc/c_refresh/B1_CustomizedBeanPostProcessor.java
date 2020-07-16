package com.imooc.c_refresh;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class B1_CustomizedBeanPostProcessor implements BeanPostProcessor {

    /**
     * 每个Bean注册后，调用后置处理器前执行此方法
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("########postProcessBeforeInitialization##"+beanName);
        return bean;
    }

    /**
     * 每个Bean注册后，调用后置处理器后执行此方法
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("########postProcessAfterInitialization##"+beanName);
        return bean;
    }
}
