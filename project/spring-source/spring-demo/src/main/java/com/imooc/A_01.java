package com.imooc;

import com.imooc.a_service.WelcomeService;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author chenpiqian
 * @date: 2020-06-10
 */
public class A_01 {
	public static void main(String[] args) {
		System.out.println("############");
		String xmlPath="D:\\spring-framework-5.2.0.RELEASE\\spring-demo\\src\\main\\resources\\spring-config.xml";
		FileSystemXmlApplicationContext appContext = new FileSystemXmlApplicationContext(xmlPath);
		WelcomeService welcomeService = appContext.getBean("welcomeService", WelcomeService.class);
		welcomeService.sayHello("成功了");
	}
}
