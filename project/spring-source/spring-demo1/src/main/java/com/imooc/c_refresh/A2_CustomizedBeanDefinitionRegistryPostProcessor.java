package com.imooc.c_refresh;

import com.imooc.b_ioc_learn.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.Configuration;

@Configuration
public class A2_CustomizedBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * BeanDefinitionRegistryPostProcessor
     * 允许在正常的BeanFactoryPostProcessor检测开始之前注册更多的自定义beandefinition
     */

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        GenericBeanDefinition definition = (GenericBeanDefinition)builder.getRawBeanDefinition();
        registry.registerBeanDefinition("user5", definition);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
