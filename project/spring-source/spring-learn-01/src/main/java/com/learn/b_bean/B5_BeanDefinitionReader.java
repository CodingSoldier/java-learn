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
		 打断点，运行代码调试
		 */
		String xmlPath="D:\\spring-framework-5.2.0.RELEASE\\spring-learn-01\\src\\main\\resources\\spring\\spring-config.xml";
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(xmlPath);

	}
}
