package com.learn.f_spring_aop;

import com.learn.f_spring_aop.a_service.A_Service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@ComponentScan("com.learn.f_spring_aop")
@EnableAspectJAutoProxy
public class FA_Run {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(FA_Run.class);
		A_Service a_Service = (A_Service)applicationContext.getBean("a_ServiceImpl");
		a_Service.sayHello();
	}

}
























