package com.zs.diveinspringboot.externalizated.configuration.bootstrap;

import com.zs.diveinspringboot.externalizated.configuration.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringXmlConfigPlaceholderBootstrap {

    public static void main(String[] args) {
        String[] locations = {"META-INF/spring/spring-context.xml","META-INF/spring/user-context.xml"};

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(locations);
        User user = applicationContext.getBean("user", User.class);
        System.out.println("用户对象 :"+user);
        //关闭上下文
        applicationContext.close();
    }
}
