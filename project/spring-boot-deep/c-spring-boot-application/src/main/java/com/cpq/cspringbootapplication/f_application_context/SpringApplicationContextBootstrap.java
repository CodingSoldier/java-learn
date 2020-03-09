package com.cpq.cspringbootapplication.f_application_context;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringApplicationContextBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SpringApplicationContextBootstrap.class)
                //.web(WebApplicationType.NONE)
                .run(args);

        // springApplication类型
        // org.springframework.boot.SpringApplication.createApplicationContext
        System.out.println("ConfigurableApplicationContext类型："+context.getClass().getName());
        System.out.println("Environment类型："+context.getEnvironment().getClass().getName());

        // 关闭上下文
        context.close();


    }

}
