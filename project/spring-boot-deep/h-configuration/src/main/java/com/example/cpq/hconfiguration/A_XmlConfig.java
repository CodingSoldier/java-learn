package com.example.cpq.hconfiguration;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class A_XmlConfig {
    public static void main(String[] args) {
/**
 外部化配置，创建一个IOC Bean，通过读取配置文件，给IOC Bean设置属性值
 读取配置文件
 新建 META-INF/default.properties
 新建 META-INF/spring/spring-context.xml、META-INF/spring/user-context.xml
 新建本类，运行
 */
        String[] locations = {"META-INF/spring/spring-context.xml", "META-INF/spring/user-context.xml"};
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext(locations);

        User user = app.getBean("user:  ", User.class);
        System.err.println("用户bean:  "+user);

        app.close();
    }
}
