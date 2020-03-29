package com.example.springbootdemo;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用@Order注解（第3点）
 */
@Order(1)
public class FirstInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * 在resources目录下新建/META-INF/spring.factories目录与文件。
     * 在resources/META-INF/spring.factories配置：
     * org.springframework.context.ApplicationContextInitializer=com.example.springbootdemo.FirstInitializer
     *
     * spring.factories的配置规则是: 接口全名=实现类全名
     */
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        /**
         * 本方法代码是添加属性 key1=value1。
         *
         * 可通过以下方式获取添加的属性：
         * @Value("${key1}")
         * String key1;
         */
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");

        // 新建属性源，并添加属性源。（第2点）
        MapPropertySource mps = new MapPropertySource("firstInitializer", map);
        environment.getPropertySources().addLast(mps);

        System.out.println("#############FirstInitializer.initialize 运行");
    }

}