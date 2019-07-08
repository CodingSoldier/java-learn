package com.cpq.b.bb;

import com.cpq.b.b.Bean01;
import org.springframework.context.annotation.Bean;

public class BB_HelloWorldConfiguration {
    @Bean
    public String helloWorld() { // 方法名即 Bean 名称
        return "Hello,World bb";
    }

    @Bean
    public Bean01 bean01() {
        return new Bean01("名字01");
    }

}
