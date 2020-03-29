package com.example.springbootdemo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

/**
 * 通过实现Ordered接口实现排序
 */
public class FirstOrderedInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("#############FirstOrderedInitializer.initialize 运行");
    }

    // 返回排序值
    @Override
    public int getOrder() {
        return 11;
    }
}