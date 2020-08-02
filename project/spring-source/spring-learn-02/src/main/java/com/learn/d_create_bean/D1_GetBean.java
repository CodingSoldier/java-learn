package com.learn.d_create_bean;

import com.learn.a_start.WelcomeController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("com.learn")
public class D1_GetBean {
	/**
	 获取Bean
		 AbstractBeanFactory#doGetBean(String, Class, Object[], boolean)
	 创建Bean
		 AbstractAutowireCapableBeanFactory#createBean(String, RootBeanDefinition, Object[])
	 		创建Bean实例，方法返回Bean实例的包装类，BeanWrapper
	 	 	AbstractAutowireCapableBeanFactory#instantiateBean(String, Object, RootBeanDefinition)
	 			创建bean
	 			beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
	 				反射无参构造函数创建实例
	 				BeanUtils#instantiateClass(Constructor, Object...)
	 */
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(D1_GetBean.class);
		WelcomeController welcomeController = (WelcomeController) applicationContext.getBean("welcomeController");
		welcomeController.handleRequest();
	}
}
