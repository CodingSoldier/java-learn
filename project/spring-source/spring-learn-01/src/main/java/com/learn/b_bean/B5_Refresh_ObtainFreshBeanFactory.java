package com.learn.b_bean;

import com.learn.a_start.WelcomeController;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;

@Configuration
@ComponentScan("com.learn")
public class B5_Refresh_ObtainFreshBeanFactory {

	/**
	 * xml方式注册BeanDefinition
	 */
	public static void main(String[] args) {
		/**
		 AbstractApplicationContext#obtainFreshBeanFactory()方法解析
		 	打断点，运行代码调试
		 	DefaultListableBeanFactory#registerBeanDefinition(String, BeanDefinition)

		往注册表注册一个新的BeanDefinition实例
		BeanDefinitionRegistry#registerBeanDefinition(String, BeanDefinition)
			移除BeanDefinition
			BeanDefinitionRegistry#removeBeanDefinition(java.lang.String)
				 BeanDefinition注册工具类
				 BeanDefinitionReaderUtils#registerBeanDefinition(BeanDefinitionHolder, BeanDefinitionRegistry)
		 */
		String xmlPath="D:\\spring-framework-5.2.0.RELEASE\\spring-learn-01\\src\\main\\resources\\spring\\spring-config.xml";
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(xmlPath);
		Object user1 = applicationContext.getBean("user1");
		System.out.println(user1);

	}

	/**
	 注解方式注册BeanDefinition
	 	 在AnnotationConfigApplicationContext构造函数中
		 打断点，运行代码调试
		 DefaultListableBeanFactory#registerBeanDefinition(String, BeanDefinition)
	 */
	@Test
	public void annotation(){
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(B5_Refresh_ObtainFreshBeanFactory.class);
		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		for(String beanDefinitionName : beanDefinitionNames){
			System.out.println(beanDefinitionName);
		}
		WelcomeController welcomeController = (WelcomeController) applicationContext.getBean("welcomeController");
		welcomeController.handleRequest();
	}
}
