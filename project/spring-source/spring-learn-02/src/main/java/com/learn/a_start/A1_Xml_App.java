package com.learn.a_start;

import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author chenpiqian
 * @date: 2020-06-10
 */
public class A1_Xml_App {
	public static void main(String[] args) {

		/**
		 * 1、build.gradle导入依赖 compile(project(":spring-context"))
		 * 2、新增WelcomeService接口与实现类
		 * 3、编写spring-config.xml，配置 <bean id="welcomeService" class="com.imooc.a_service.WelcomeServiceImpl"/>
		 * 4、通过以下代码获取bean，并调用sayHello方法
		 *
		 * 到此，证明我们自己编译spring源码，使用自己编译的jar包成功
		 */
		System.out.println("############");
		String xmlPath="D:\\spring-framework-5.2.0.RELEASE\\spring-learn-01\\src\\main\\resources\\spring\\spring-config.xml";
		FileSystemXmlApplicationContext appContext = new FileSystemXmlApplicationContext(xmlPath);
		WelcomeService welcomeService = appContext.getBean("welcomeService", WelcomeService.class);
		welcomeService.sayHello("成功了");

	}
}
