package com.learn.c_refresh;

import com.learn.a_start.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

/**
 * 对BeanDefinitionRegistry做后置处理，往BeanDefinitionRegistry中注册bean
 */
@Configuration
public class C1_BeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		Class<?> clazz = User.class;
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		GenericBeanDefinition definition = (GenericBeanDefinition)builder.getRawBeanDefinition();
		registry.registerBeanDefinition("user5", definition);
		registry.registerBeanDefinition("user6", definition);
		System.out.println("BeanDefinitionRegistryPostProcessor###postProcessBeanDefinitionRegistry");
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

	}
}
