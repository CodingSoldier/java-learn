package com.example.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootDemoApplication.class, args);

        Bean01 bean01 =  context.getBean(Bean01.class);
        System.out.println("获取bean01   "+ bean01.toString());

        // 关闭上下文
        context.close();
    }
}
