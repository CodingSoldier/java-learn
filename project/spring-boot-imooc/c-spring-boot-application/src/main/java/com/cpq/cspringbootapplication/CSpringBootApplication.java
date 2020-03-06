package com.cpq.cspringbootapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashSet;
import java.util.Set;


public class CSpringBootApplication {

    public static void main(String[] args) {

/**
 * 简单方式启动工程，使用ApplicationConfiguration作为配置源。ApplicationConfiguration.java必须加上注解@SpringBootApplication
   @SpringBootApplication会触发：
     @SpringBootConfiguration
     @EnableAutoConfiguration
     @ComponentScan
   通过自动装配的方式启动spring-boot应用
 */
        //SpringApplication.run(ApplicationConfiguration.class, args);


        /**
         * api方式启动工程，可以配置多个启动源
         */
        SpringApplication application = new SpringApplication();

        /**
         * 设置应用类型是非web类型。
         * 推断应用类型是在SpringApplication构造函数中做的，创建SpringApplication实例后再改变应用类型
         * 非web类型，程序运行完后线程终止
         */
        //application.setWebApplicationType(WebApplicationType.NONE);

        /**
         * new SpringApplication();构造函数中通过线程堆栈StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();推断引导类。
         * 创建SpringApplication后，手动修改引导类Main Class
         */
        Set<String> sources = new HashSet<>();
        sources.add(ApplicationConfiguration.class.getName());
        application.setSources(sources);

        ConfigurableApplicationContext context = application.run(args);
        System.out.println("ioc bean  "+context.getBean(ApplicationConfiguration.class));

    }

    /**
     * 加上了@SpringBootApplication的类都可以作为配置源
     */
    @SpringBootApplication
    public static class ApplicationConfiguration{

    }

}
