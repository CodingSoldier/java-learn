package com.learn.b_bean;


import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

import java.io.*;

public class B4_Resource {

	/**
	 Resource接口是spring对资源的抽象，具体资源由相应的实现类来定义

	 类图依赖关系
	 	Resource
	 		AbstractResource
	 			ServletContextResource   Web容器上下文中的资源，访问jar包、war包资源
	 			ClassPathResource        类路径下的资源，访问jar包、war包资源
	 			FileSystemResource       文件系统资源
	 	这个是典型的spring设计类
	 	1、定义一个抽象接口
	 	2、创建一个抽象类实现接口，并实现公共方法
	 	3、具体实现类继承抽象类，实现自定义方法，拓展自身功能
	 */

	@Test
	public void fileResource() throws IOException {
		String path = "D:\\spring-framework-5.2.0.RELEASE\\spring-learn-01\\src\\main\\java\\com\\learn\\b_bean\\B4_text.txt";
		FileSystemResource fileSystemResource = new FileSystemResource(path);
		File file = fileSystemResource.getFile();
		System.out.println("文件长度 "+file.length());

		OutputStream outputStream = fileSystemResource.getOutputStream();
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		bufferedWriter.write("新写入内容");
		bufferedWriter.flush();
		outputStream.close();
		outputStreamWriter.close();
		bufferedWriter.close();

	}

/**
 spring提供了强大的加载资源的方式
	自动识别 classpath:  file: 等资源地址前缀
	支持自动解析Ant风格的资源地址

 Ant 路径匹配表达式，用来对URI进行匹配
	 ?   匹配任何单字符
	 *   匹配0或任意数量的字符
	 **  匹配0或多个目录

 spring使用ResourceLoader实现不同的加载策略，按需返回特定类型的Resource资源
 	根据资源路径返回资源实现类，例如：
 	以 classpath: 开头，则返回ClassPathResource实例
 	以 / 开头，则返回ClassPathContextResource实例
	org.springframework.core.io.DefaultResourceLoader#getResource(java.lang.String)

 ResourceLoader
 	ResourcePatternResolver
 	getResources()方法支持Ant风格路径解析
 		PathMatchingResourcePatternResolver
 		具体实现


 ApplicationContext
	 AbstractApplicationContext
		 继承了DefaultResourceLoader
		 并且有属性 private ResourcePatternResolver resourcePatternResolver;
		 所以AbstractApplicationContext通过组合集成了资源加载功能
 */

}



















































