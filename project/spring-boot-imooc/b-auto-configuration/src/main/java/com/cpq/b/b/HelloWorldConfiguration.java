package com.cpq.b.b;

import org.springframework.context.annotation.Bean;

public class HelloWorldConfiguration {
    @Bean
    public String helloWorld() { // 方法名即 Bean 名称
        return "Hello,World 2018";
    }
}
