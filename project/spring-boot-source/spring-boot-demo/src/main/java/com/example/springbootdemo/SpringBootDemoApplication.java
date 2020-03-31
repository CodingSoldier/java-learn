package com.example.springbootdemo;

import com.example.springbootdemo.initializer.SecondInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootDemoApplication {

    public static void main(String[] args) {

        //SpringApplication.run(SpringBootDemoApplication.class, args);

        // 第2中添加系统初始化器的方式
        SpringApplication springApplication = new SpringApplication(SpringBootDemoApplication.class);
        springApplication.addInitializers(new SecondInitializer());
        springApplication.run(args);  // 这句代码打上断点调试
    }



}
