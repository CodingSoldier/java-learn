package com.learn.e_aop.a_controller;

import com.learn.e_aop.b_advice.LittleUniverse;
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

		// 测试切面
		//A_Controller a_Controller = (A_Controller) applicationContext.getBean("a_Controller");
		//a_Controller.test01();


		LittleUniverse a_controller = (LittleUniverse)applicationContext.getBean("a_Controller");
		a_controller.burningup();

	}

	/**
	 	织入：将Aspect模块化的 横切关注点集成到OOP里
	 	织入器：完成织入过程的执行者，如ajc
	 	Spring AOP会使用一组类来作为织入器以完成最终的织入操作
	 */
}
























