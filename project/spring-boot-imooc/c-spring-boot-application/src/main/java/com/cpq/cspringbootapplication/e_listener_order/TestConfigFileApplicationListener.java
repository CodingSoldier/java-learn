package com.cpq.cspringbootapplication.e_listener_order;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

public class TestConfigFileApplicationListener implements SmartApplicationListener, Ordered {

    /**
     * springboot通过事件监听来读取properties文件
     * ConfigFileApplicationListener读取配置文件，源码的order是Ordered.HIGHEST_PRECEDENCE + 10
     *
     * @return
     */
    @Override
    public int getOrder() {
        // 在ConfigFileApplicationListener之前执行，无法读取到属性值
        //return Ordered.HIGHEST_PRECEDENCE + 9;

        // 在ConfigFileApplicationListener之后执行，能读取到属性值
        return Ordered.HIGHEST_PRECEDENCE + 11;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(eventType)
                || ApplicationPreparedEvent.class.isAssignableFrom(eventType);
    }

    public boolean supportsSourceType(Class<?> aClass) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            ApplicationEnvironmentPreparedEvent pe = (ApplicationEnvironmentPreparedEvent) event;
           Environment environment = pe.getEnvironment();
            System.out.println("读取是属性值e.listener.order："+environment.getProperty("e.listener.order"));

        }
        if (event instanceof ApplicationPreparedEvent) {
        }
    }

}
