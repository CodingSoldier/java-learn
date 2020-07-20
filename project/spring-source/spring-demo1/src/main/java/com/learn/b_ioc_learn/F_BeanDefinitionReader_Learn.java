package com.learn.b_ioc_learn;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.io.IOException;

public class F_BeanDefinitionReader_Learn {

	/**
	 BeanDefinitionReader是ResourceLoader的使用者
	 */
	@Test
	public void testXmlBeanDefinitionReader() throws IOException {

		String xmlPath = "D:\\third-code\\java-learn\\project\\spring-source\\spring-demo1\\src\\main\\resources\\spring-config.xml";
		ApplicationContext app = new FileSystemXmlApplicationContext(xmlPath);
		Object user1 = app.getBean("user1");
		System.out.println("####"+user1);

/**
 在 XmlBeanDefinitionReader#loadBeanDefinitions()打断点
 FFF_XmlBeanDefinitionReader#loadBeanDefinitions()

 new FileSystemXmlApplicationContext(xmlPath) 解析

 	AbstractBeanDefinitionReader#loadBeanDefinitions(java.lang.String, java.util.Set)
 	通过路径前缀查找xmlPath对应ResourcePatternResolver，并创建对应的Resource实例

 		org.springframework.beans.factory.xml.XmlBeanDefinitionReader#loadBeanDefinitions(EncodedResource)
 		使用XmlBeanDefinitionReader加载xml中的bean定义

		 	XmlBeanDefinitionReader# tions(InputSource, Resource)
		 	加载Bean定义，在spring中，以do开头，就是真正做事情的方法
		 	Document doc = doLoadDocument(inputSource, resource);
		 	doc是spring-config.xml，docElement是xml文档元素

		 		XmlBeanDefinitionReader#registerBeanDefinitions(Document, Resource)
		 		注册bean

		 			DefaultListableBeanFactory#getBeanDefinitionCount()
		 			this.beanDefinitionMap 是 new ConcurrentHashMap<>(256);

		 				DefaultBeanDefinitionDocumentReader#doRegisterBeanDefinitions(Element)
		 				执行bean注册

							BeanDefinitionParserDelegate#parseBeanDefinitionElement(Element, BeanDefinition)
 							解析xml配置的bean

 								BeanDefinitionReaderUtils#createBeanDefinition(String, String, ClassLoader)
 								创建GenericBeanDefinition，GenericBeanDefinition已经成为spring通用的bean实现
 								比如设置setBeanClass、setBeanClassName

						DefaultBeanDefinitionDocumentReader#processBeanDefinition(Element, BeanDefinitionParserDelegate)
 						往容器注册bean
						 //向Spring IOC容器注册解析得到的BeanDefinition，这是BeanDefinition向IOC容器注册的入口
						 BeanDefinitionReaderUtils.registerBeanDefinition(bdHolder, getReaderContext().getRegistry());
						 // 在完成BeanDefinition注册之后，往容器发送注册完成的事件
						 getReaderContext().fireComponentRegistered(new BeanComponentDefinition(bdHolder));


					BeanDefinitionReaderUtils#registerBeanDefinition(BeanDefinitionHolder, BeanDefinitionRegistry)
 					registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());
 					通过beanName、bean实例注册bean

 DefaultListableBeanFactory#registerBeanDefinition(java.lang.String, org.springframework.beans.factory.config.BeanDefinition)
 注册bean



 DefaultListableBeanFactory#registerBeanDefinition(String, BeanDefinition)
 打断点，bean注册最里层方法。
	 AnnotationConfigApplicationContext applicationcontext = new AnnotationConfigApplicationContext(A_Entrance.class);
	 注解方式最里层代码也是这样

 bean注册的本质是这句代码
 	this.beanDefinitionMap.put(beanName, beanDefinition);
 	this.beanDefinitionMap 是 new ConcurrentHashMap<>(256);

 */
	}
}
