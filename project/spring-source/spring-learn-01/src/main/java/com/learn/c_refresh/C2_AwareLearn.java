package com.learn.c_refresh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.learn.c_refresh")
public class C2_AwareLearn {

	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(C2_AwareLearn.class);
		C2_Aware c2_Aware = (C2_Aware)applicationContext.getBean("c2_Aware");
		c2_Aware.testAware();
	}

}
