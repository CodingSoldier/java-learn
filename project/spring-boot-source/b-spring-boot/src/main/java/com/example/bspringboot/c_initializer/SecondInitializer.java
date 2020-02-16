package com.example.bspringboot.c_initializer;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

@Order(2)
public class SecondInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * 第二种配置初始化器的方式，在 BSpringBootApplication#main()中添加：
         SpringApplication springApplication = new SpringApplication(BSpringBootApplication.class);
         springApplication.addInitializers(new SecondInitializer());
         springApplication.run(args);

     源码调用栈跟FirstInitializer.java可以说是一样的，同样是启动类的run()调用
         initialize:39, FirstInitializer (com.example.bspringboot.c_initializer)
         applyInitializers:626, SpringApplication (org.springframework.boot)
         prepareContext:370, SpringApplication (org.springframework.boot)
         run:314, SpringApplication (org.springframework.boot)
         main:17, BSpringBootApplication (com.example.bspringboot)
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        Map<String, Object> map = new HashMap<>();
        map.put("key2", "value2");
        MapPropertySource mps = new MapPropertySource("secondInitializer", map);
        environment.getPropertySources().addLast(mps);
        System.out.println("#############SecondInitializer.initialize 运行");
    }

}
