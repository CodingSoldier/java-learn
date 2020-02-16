package com.example.bspringboot.c_initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Order(3)
public class ThirdInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * 第3种配置初始化器的方式，application.properties添加
     * context.initializer.classes=com.example.bspringboot.c_initializer.ThirdInitializer
     *
     * 初始化顺序
     #############ThirdInitializer.initialize 运行
     #############FirstInitializer.initialize 运行
     #############SecondInitializer.initialize 运行
     ThirdInitializer反而最先运行

     源码调用栈：
     initialize:27, ThirdInitializer (com.example.bspringboot.c_initializer)
     applyInitializers:107, DelegatingApplicationContextInitializer (org.springframework.boot.context.config)
        调用ThirdInitializer#initialize()
     applyInitializerClasses:88, DelegatingApplicationContextInitializer (org.springframework.boot.context.config)
     initialize:56, DelegatingApplicationContextInitializer (org.springframework.boot.context.config)
        # spring-boot-2.2.4.RELEASE.jar!\META-INF\spring.factories配置类DelegatingApplicationContextInitializer初始化器。DelegatingApplicationContextInitializer的order=0，最先被调用
        # List<Class<?>> initializerClasses = getInitializerClasses(environment);会创建context.initializer.classes属性指定的类集合，则ThirdInitializer会创建
     applyInitializers:626, SpringApplication (org.springframework.boot)
     prepareContext:370, SpringApplication (org.springframework.boot)
     run:314, SpringApplication (org.springframework.boot)
     run:1226, SpringApplication (org.springframework.boot)
     run:1215, SpringApplication (org.springframework.boot)
     main:12, BSpringBootApplication (com.example.bspringboot)  同样是启动类run方法调用
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Map<String, Object> map = new HashMap<>();
        map.put("key3", "value3");
        MapPropertySource mps = new MapPropertySource("thirdInitializer", map);
        environment.getPropertySources().addLast(mps);
        System.out.println("#############ThirdInitializer.initialize 运行");
    }

}
