package com.learn;

import com.learn.a_controller.WelcomeController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.learn")
public class E_CreateBean {

	/**
	 * 创建bean，
	 * 316行，打断点org.springframework.beans.factory.support.AbstractBeanFactory#createBean()
	 *
	 */
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(E_CreateBean.class);
		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		for(String beanDefinitionName : beanDefinitionNames){
			System.out.println(beanDefinitionName);
		}
		WelcomeController welcomeController = (WelcomeController) applicationContext.getBean("welcomeController");

	}
}
