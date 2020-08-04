package com.learn.c_refresh;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.learn.c_refresh")
public class C1_PostProcessor {
	/**
	 后置处理器PostProcessor
		本身也是一种需要注册到容器里的Bean
		其里面的方法会在特定时机被容器调用
		实现不改变容器或者Bean核心逻辑情况下对Bean进行拓展
		对Bean进行包装，影响其行为、修改Bean的内容

	 容器级别的后置处理器
		BeanDefinitionRegistryPostProcessor
		BeanFactoryPostProcessor
	 Bean级别的后置处理器
		BeanPostProcessor
	 */
	public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(C1_PostProcessor.class);
		Object user5 = applicationContext.getBean("user5");
		System.out.println("###"+user5);
		Object user6 = applicationContext.getBean("user6");
		System.out.println("###"+user6);
	}

}
