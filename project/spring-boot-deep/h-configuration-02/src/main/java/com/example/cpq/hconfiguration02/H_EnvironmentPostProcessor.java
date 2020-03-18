package com.example.cpq.hconfiguration02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;

public class H_EnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public static Integer ORDER = Ordered.LOWEST_PRECEDENCE;

    /**
     * 改Order顺序没用，始终会被G_EnvironmentPreparedEventListener#onApplicationEvent()覆盖
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        System.out.println("################H_EnvironmentPostProcessor#postProcessEnvironment");
        System.out.println("################H_EnvironmentPostProcessor#ORDER "+ORDER);
        MutablePropertySources propertySources = environment.getPropertySources();
        HashMap<String, Object> source = new HashMap<>(16);
        source.put("user.city.name", "H_EnvironmentPostProcessor.postProcessEnvironment");
        MapPropertySource onApplicationEvent = new MapPropertySource("postProcessEnvironment", source);
        propertySources.addFirst(onApplicationEvent);
    }

    @Override
    public int getOrder() {
        return ORDER ;
    }
}
