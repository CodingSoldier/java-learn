package com.imooc.a_controller;

import com.imooc.a_service.WelcomeService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;

@Controller
public class WelcomeController implements ApplicationContextAware, BeanNameAware {
	private String myName;
	private ApplicationContext myContainer;
	@Autowired
	private WelcomeService welcomeService;
	public void handleRequest(){
		welcomeService.sayHello("来自Controller的问候");
		System.out.println("我是谁：" + myName);
		String[] beanDefinitionNames = myContainer.getBeanDefinitionNames();
		for(String beanDefinitionName : beanDefinitionNames) {
			System.out.println("召唤容器神兽，通过神兽获得：" + beanDefinitionName);
		}
	}

	@Override
	public void setBeanName(String name) {
		this.myName = name;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.myContainer = applicationContext;
	}
}
