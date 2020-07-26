package com.learn.d_create_bean;

import com.learn.d_create_bean.mode.BoyFriend;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("com.learn.d_create_bean")
public class D2_GetBean {

	/**
	 Spring是否支持所有循环依赖的情况
	 构造器循环依赖      singleton、prototype
	 Setter注入循环依赖  singleton、prototype

	 prototype的bean不支持循环依赖
	 	构造器方式注入bean、Setter方式注入bean都不支持
	 	因为spring创建bean的三级缓存要保证单例bean的唯一性，缓存key使用的是beanName，就无法支持Prototype的循环依赖

	 singleton的bean也不支持构造器注入
	 spring只支持singleton的Setter方式的循环依赖
	 */
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(D2_GetBean.class);
		BoyFriend boyFriend = (BoyFriend) applicationContext.getBean("boyFriend");
	}

}
