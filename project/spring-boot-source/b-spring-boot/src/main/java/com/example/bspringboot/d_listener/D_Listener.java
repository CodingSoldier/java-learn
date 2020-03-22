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

    // 断点调试
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
