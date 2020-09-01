//package com.test.config2;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
///**
// * DispatcherServlet中的bean
// */
//@Configuration
//@ComponentScan("com.test.controller")
//@EnableWebMvc
//public class MvcConfig {
//
//	@Bean
//	public InternalResourceViewResolver viewResolver(){
//		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
//		internalResourceViewResolver.setPrefix("/WEB-INF/jsp/");
//		internalResourceViewResolver.setSuffix(".jsp");
//		return internalResourceViewResolver;
//	}
//
//}
