package com.example.bspringboot.c_initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * order值越小越先执行
 */
@Order(1)
public class FirstInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {


    /**
     * 自定义系统初始化器，配置spring属性
     * 需要添加 META-INF/spring.factories
     * org.springframework.context.ApplicationContextInitializer=com.example.bspringboot.c_initializer.FirstInitializer
     * org.springframework.context.ApplicationContextInitializer正式ApplicationContextInitializer接口的全名
     * 通过 applicationContext.getEnvironment().getProperty("key1"); 获取环境配置属性
     *
     * 打断点，分析源码调用栈
     * initialize:29, FirstInitializer (com.example.bspringboot.c_initializer)
     * applyInitializers:626, SpringApplication (org.springframework.boot)
     *     for循环所有ApplicationContextInitializer，调用本实现类的initialize()方法
     * prepareContext:370, SpringApplication (org.springframework.boot)
     *     在刷新spring容器上下文refreshContext(context)前调用，准备上下文，参考图片：3-调用点.png
     * run:314, SpringApplication (org.springframework.boot)          SpringApplication中的run方法
     * run:1226, SpringApplication (org.springframework.boot)         SpringApplication中的run方法
     * run:1215, SpringApplication (org.springframework.boot)         SpringApplication中的run方法
     * main:12, BSpringBootApplication (com.example.bspringboot)      启动类中的run()方法
     *
     * 推荐使用这种方式添加系统初始化器
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        MapPropertySource mps = new MapPropertySource("firstInitializer", map);
        environment.getPropertySources().addLast(mps);
        System.out.println("#############FirstInitializer.initialize 运行");
    }

}
