package com.learn.b_ioc_learn;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.Arrays;

/**
 * @author chenpiqian
 * @date: 2020-07-14
 */
@Configuration
@ComponentScan("com.learn")
public class C_BeanFactoryLearn {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        String xmlPath = "D:\\third-code\\java-learn\\project\\spring-source\\spring-demo1\\src\\main\\resources\\spring-config.xml";
        ApplicationContext app = new FileSystemXmlApplicationContext(xmlPath);

        System.out.println("\n##############\n");
        /**
         * BeanFactory接口方法测试
         */
        String[] user1s = app.getAliases("user1");
        System.out.println("#######getAliases#######"+Arrays.toString(user1s));
        Class<?> user2 = app.getType("user2");
        System.out.println("#######getType#######"+user2);
        boolean isTypeMatch = app.isTypeMatch("user1", User.class);
        System.out.println("#######isTypeMatch#######"+isTypeMatch);


    }

}
