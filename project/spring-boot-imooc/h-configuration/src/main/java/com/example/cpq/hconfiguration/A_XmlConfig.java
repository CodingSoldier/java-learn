package com.example.cpq.hconfiguration;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
    读取配置文件
 */
public class A_XmlConfig {
    public static void main(String[] args) {
        //String[] locations = { "META-INF/spring/user-context.xml"};
        String[] locations = {"META-INF/spring/spring-context.xml", "META-INF/spring/user-context.xml"};
        ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext(locations);

        User user = app.getBean("user", User.class);
        System.err.println("用户bean"+user);

        app.close();
    }
}
