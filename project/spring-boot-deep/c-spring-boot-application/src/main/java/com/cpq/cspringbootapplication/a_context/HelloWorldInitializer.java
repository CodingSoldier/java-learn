package com.cpq.cspringbootapplication.a_context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**

系统初始化器



public class HelloWorldInitializer implements ApplicationContextInitializer {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

    }
}


 */

// 排序方式1，使用@Order注解
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HelloWorldInitializer<C extends ConfigurableApplicationContext> implements ApplicationContextInitializer<C> {

    @Override
    public void initialize(C applicationContext) {
        System.out.println("启动 HelloWorldInitializer");
    }

}
