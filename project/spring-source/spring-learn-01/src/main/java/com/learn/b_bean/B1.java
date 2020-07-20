package com.learn.b_bean;

import com.learn.a_start.User;
import com.learn.b_bean.factory.PrefixUserFactoryBean;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class B1 {
	/**
	 jdk通过java.lang.Class描述对象
	 spring通过BeanDefinition描述bean
	 scope（@Scope）作用范围：
		 singleton单例。
		 prototype每次getBean()就创建一个，相当于new
		 request
		 session
		 ......
		 lazy-init（@Lazy）是否懒加载：
		 首选primary（@Primary）优先注入的类
		 factory-bean和factory-method（@Configuration和@Bean）
	 */

	public static void main(String[] args) {
		System.out.println("############");
		String xmlPath="D:\\spring-framework-5.2.0.RELEASE\\spring-learn-01\\src\\main\\resources\\spring\\spring-config.xml";
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(xmlPath);

		//得到无参构造函数创建的对象:
		User user1a = (User) applicationContext.getBean("user1");
		User user1b = (User) applicationContext.getBean("user1");
//得到静态工厂创建的对象：
		User user2a = (User) applicationContext.getBean("user2");
		User user2c = (User) applicationContext.getBean("user2");
//得到实例工厂创建的对象：
		User user3a = (User) applicationContext.getBean("user3");
		User user3b = (User) applicationContext.getBean("user3");

		//得到beanFactory创建的对象：
		PrefixUserFactoryBean userFactoryBean4a = (PrefixUserFactoryBean) applicationContext.getBean("&userFactoryBean");
		User user4b = (User) applicationContext.getBean("userFactoryBean");

		System.out.println("无参构造函数创建的对象:" + user1a);
		System.out.println("无参构造函数创建的对象:" + user1b);
		System.out.println("静态工厂创建的对象：" + user2a);
		System.out.println("静态工厂创建的对象：" + user2c);
		System.out.println("实例工厂创建的对象：" + user3a);
		System.out.println("实例工厂创建的对象：" + user3b);
		System.out.println("factorybean对象：" + userFactoryBean4a);
		System.out.println("factorybean创建的对象：" + user4b);
	}
}
