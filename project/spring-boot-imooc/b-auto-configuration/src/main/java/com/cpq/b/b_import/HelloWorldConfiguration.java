package com.cpq.b.b_import;

import org.springframework.context.annotation.Bean;

public class HelloWorldConfiguration {
    @Bean
    public String helloWorld() { // 方法名即 Bean 名称
        return "Hello,World 2018";
    }

    @Bean
    public Bean01 bean01() {
        return new Bean01("名字01");
    }

}
