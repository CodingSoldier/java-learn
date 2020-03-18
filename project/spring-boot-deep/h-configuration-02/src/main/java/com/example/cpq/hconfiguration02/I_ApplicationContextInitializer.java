package com.example.cpq.hconfiguration02;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;

/**
 * @author chenpiqian
 * @date: 2020-03-18
 */
public class I_ApplicationContextInitializer implements ApplicationContextInitializer {

    // initialize最后执行，环境变量会是本方法设置的值
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.out.println("################I_ApplicationContextInitializer#initialize");
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        HashMap<String, Object> source = new HashMap<>(16);
        source.put("user.city.name", "I_ApplicationContextInitializer.initialize");
        MapPropertySource onApplicationEvent = new MapPropertySource("initialize", source);
        propertySources.addFirst(onApplicationEvent);
    }
}
