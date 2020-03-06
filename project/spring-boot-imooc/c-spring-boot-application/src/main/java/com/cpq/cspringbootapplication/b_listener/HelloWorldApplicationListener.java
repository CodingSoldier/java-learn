package com.cpq.cspringbootapplication.b_listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 ContextRefreshedEvent事件继承关系：
 Object (java.lang)
     EventObject (java.util)
         ApplicationEvent (org.springframework.context)
             ApplicationContextEvent (org.springframework.context.event)
                 ContextRefreshedEvent (org.springframework.context.event)

 ApplicationListener<E extends ApplicationEvent>
    ContextRefreshedEvent 是 ApplicationEvent 子类
*/
@Order(Ordered.HIGHEST_PRECEDENCE)
public class HelloWorldApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("事件监听HelloWorldApplicationListener  "+event.getSource());
    }
}
