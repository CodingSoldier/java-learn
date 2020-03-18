package com.example.cpq.hconfiguration02;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;

public class G_EnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    /**
     * F_RunListener#getOrder() = 1
     * 本监听先运行，properties值被F_RunListener#environmentPrepared()覆盖
     *
     * F_RunListener#getOrder() = -1
     * F_RunListener#environmentPrepared()先运行，properties值被本方法覆盖
     *
     */
    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        System.out.println("################G_EnvironmentPreparedEventListener#onApplicationEvent");
        ConfigurableEnvironment environment = event.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        HashMap<String, Object> source = new HashMap<>(16);
        source.put("user.city.name", "G_EnvironmentPreparedEventListener.onApplicationEvent");
        MapPropertySource onApplicationEvent = new MapPropertySource("onApplicationEvent", source);
        propertySources.addFirst(onApplicationEvent);
    }
}
