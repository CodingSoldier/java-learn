package com.learn.c_refresh;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * BeanPostProcessor对具体Bean做后置处理，可包装bean，增强bean功能
 */
@Configuration
public class C1_BeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("BeanPostProcessor#before###beanName  "+beanName);
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("BeanPostProcessor#after###beanName  "+beanName);
		return bean;
	}

}
