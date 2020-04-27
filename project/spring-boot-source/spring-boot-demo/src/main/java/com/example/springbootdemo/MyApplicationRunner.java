package com.example.springbootdemo;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Value("${test.property}")
    String testProperty;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("###输出test.property："+testProperty);


        System.out.println("###输出test.property："+MyEnvironmentAware.getProperty("test.property"));

        System.out.println("###输出test.placeholder："+MyEnvironmentAware.getProperty("test.placeholder"));
    }
}
