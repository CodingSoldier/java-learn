package com.learn.b_bean;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class B5_BeanDefinitionReader {
	/**
	 * BeanDefinitionReader
	 * 持有ResourceLoader，记载配置文件，例如xml，将xml配置bean转换为BeanDefinition
	 * <p>
	 * 重点代码
	 * org.springframework.beans.factory.support.AbstractBeanDefinitionReader#loadBeanDefinitions(java.lang.String, java.util.Set)
	 */

	public static void main(String[] args) {
		/**
		 org.springframework.beans.factory.xml.XmlBeanDefinitionReader#loadBeanDefinitions(org.springframework.core.io.Resource)
		 org.springframework.beans.factory.xml.XmlBeanDefinitionReader#registerBeanDefinitions(org.w3c.dom.Document, org.springframework.core.io.Resource)
		 org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader#processBeanDefinition(org.w3c.dom.Element, org.springframework.beans.factory.xml.BeanDefinitionParserDelegate)

		 打断点，运行代码调试

		 往注册表注册一个新的BeanDefinition实例
		 org.springframework.beans.factory.support.BeanDefinitionRegistry#registerBeanDefinition(java.lang.String, org.springframework.beans.factory.config.BeanDefinition)

		 移除BeanDefinition
		 org.springframework.beans.factory.support.BeanDefinitionRegistry#removeBeanDefinition(java.lang.String)

		 BeanDefinition注册工具类
		 org.springframework.beans.factory.support.BeanDefinitionReaderUtils#registerBeanDefinition(org.springframework.beans.factory.config.BeanDefinitionHolder, org.springframework.beans.factory.support.BeanDefinitionRegistry)
		 */
		String xmlPath="D:\\spring-framework-5.2.0.RELEASE\\spring-learn-01\\src\\main\\resources\\spring\\spring-config.xml";
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(xmlPath);
		Object user1 = applicationContext.getBean("user1");
		System.out.println(user1);


	}
}
