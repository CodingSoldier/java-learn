package com.imooc.b_ioc_learn;

import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.AntPathMatcher;

import java.io.*;

/**
 * @author chenpiqian
 * @date: 2020-07-16
 */
public class E_ResourceLearn {

    @Test
    public void fileSystemResource() throws IOException {
        FileSystemResource fileSystemResource = new FileSystemResource("D:\\third-code\\java-learn\\project\\spring-source\\spring-demo1\\src\\main\\java\\com\\imooc\\b_ioc_learn\\E_ResourceLearn.txt");
        File file = fileSystemResource.getFile();
        System.out.println("#########"+file.length());
        OutputStream outputStream = fileSystemResource.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        bufferedWriter.write("代码写入内容");
        bufferedWriter.flush();
        outputStream.close();
        bufferedWriter.close();
    }

    /**
     根据资源地址自动选择正确的Resource
        自动识别 classpath:   file:  等资源地址前缀
        支持自动解析Ant风格带通配符的资源地址

     Ant 路径匹配表达式，用来对URI进行匹配
        ?   匹配任何单字符
        *   匹配0或任意数量的字符
        **  匹配0或多个目录

     自动判断路径的开头，返回ResourceLoader
        com.imooc.b_ioc_learn.E_DefaultResourceLoader#getResource(java.lang.String)

     ApplicationContext
        AbstractApplicationContext
            继承了DefaultResourceLoader
            并且有属性 private ResourcePatternResolver resourcePatternResolver;
            所以AbstractApplicationContext通过组合集成了资源加载功能


     */
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Test
    public void ant(){
        boolean match = antPathMatcher.match("/**/info", "//info");
        System.out.println("#########"+match);
        match = antPathMatcher.match("/**.jsp", "/.jsp");
        System.out.println("#########"+match);
    }



}
