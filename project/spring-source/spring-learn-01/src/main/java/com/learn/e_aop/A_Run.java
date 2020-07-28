package com.learn.e_aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@ComponentScan("com.learn.e_aop")
@EnableAspectJAutoProxy
public class A_Run {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(A_Run.class);
		A_Controller a_Controller = (A_Controller) applicationContext.getBean("a_Controller");
		a_Controller.test01();
	}
}
