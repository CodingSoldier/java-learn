package com.cpq.cspringbootapplication.d_runlistener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class HelloWorldRunListener implements SpringApplicationRunListener {

    /**
     * 实现接口所有方法还不行，必须加上此构造函数，因为源码会使用此构造函数创建实例
     * 将此实现类配置到spring.factories文件中
     */
    public HelloWorldRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
        System.out.println("运行 HelloWorldRunListener");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
