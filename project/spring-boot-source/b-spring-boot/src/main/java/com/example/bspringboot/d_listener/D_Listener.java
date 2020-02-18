package com.example.bspringboot.d_listener;

import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * SmartApplicationListener是ApplicationListener接口的拓展体，支持更多的事件源
 */
@Order(4)
public class D_Listener implements SmartApplicationListener {

/**
 * 监听的事件
 supportsEventType:20, D_Listener (com.example.bspringboot.d_listener)
 supportsEventType:72, GenericApplicationListenerAdapter (org.springframework.context.event)
 supportsEvent:353, AbstractApplicationEventMulticaster (org.springframework.context.event)
 retrieveApplicationListeners:229, AbstractApplicationEventMulticaster (org.springframework.context.event)
 getApplicationListeners:197, AbstractApplicationEventMulticaster (org.springframework.context.event)
 multicastEvent:134, SimpleApplicationEventMulticaster (org.springframework.context.event)
 multicastEvent:127, SimpleApplicationEventMulticaster (org.springframework.context.event)
 starting:70, EventPublishingRunListener (org.springframework.boot.context.event)
 starting:47, SpringApplicationRunListeners (org.springframework.boot)
 run:305, SpringApplication (org.springframework.boot)
 main:24, BSpringBootApplication (com.example.bspringboot)
 */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationPreparedEvent.class.isAssignableFrom(eventType)
        || ApplicationStartedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("########D_Listener事件 "+event.getClass().toString());
    }
}
