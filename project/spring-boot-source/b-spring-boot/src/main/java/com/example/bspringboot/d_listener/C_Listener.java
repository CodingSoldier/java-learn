package com.example.bspringboot.d_listener;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;

/**
 * 在配置文件中添加
 * context.listener.classes=com.example.bspringboot.d_listener.C_Listener
 */
@Order(3)
public class C_Listener implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        System.out.println("####### C_Listener##ApplicationStartedEvent");
    }
}
