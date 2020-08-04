package com.learn.b_bean;

public class B3_Context {

	/**
	 在spring中简单容器称为XXXFactory，
	 高级容器称为XXXContext，高级容器添加除了bean以外的额外功能

	 ApplicationContext常用容器
	 	XML：
	 		FileSystemXmlApplicationContext  从文件系统加载配置
	 		ClassPathXmlApplicationContext   从classpath加载配置
	 		XmlWebApplicationContext         用于web应用程序的容器
	 	注解：
	 		AnnotationConfigServletWebServerApplicationContext   基于servlet
	 		AnnotationConfigReactiveWebServerApplicationContext  reactive环境
	 		AnnotationConfigApplicationContext     非web环境

	 重点方法
	 org.springframework.context.support.AbstractApplicationContext#refresh()
	 */

}
