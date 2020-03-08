package com.cpq.cspringbootapplication.c_event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * spring 事件
 */
public class A_BootstrapSpringApplication {

    public static void main(String[] args) {

        // 创建上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 注册应用事件监听器
        context.addApplicationListener(new ApplicationListener(){
            @Override
            public void onApplicationEvent(ApplicationEvent applicationEvent) {
                System.out.println("监听到事件："+applicationEvent);
            }
        });

        //启动上线文
        context.refresh();

        // 发送事件
        context.publishEvent("HelloWorld");
        context.publishEvent(new ApplicationEvent("先发送事件") {

        });

        // 关闭上下文
        context.close();
    }

}
