package com.learn.c_refresh;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


@Component
public class C2_Aware implements ApplicationContextAware, BeanNameAware {

    /**
     * Aware是获取本bean（C2_Aware）的容器，
     * 例如通过BeanNameAware获取bean在容器中的名称
     * ApplicationContextAware获取bean对应的applicationContext
     *
     * 正常情况下，bean不会感知到容器的存在，bean与容器是解耦的
     * 使用Aware，bean能获取容器信息，导致bean与容器耦合了
     */
    private String beanName;
    private ApplicationContext applicationContext;

    public void testAware(){
        System.out.println("#####beanName###"+beanName);
        System.out.println("#####applicationContext###"+applicationContext);
    }

    @Override
    public void setBeanName(String name) {
		System.out.println("#####C2_Aware#setBeanName###"+name);
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("#####C2_Aware#setApplicationContext###"+applicationContext);
        this.applicationContext = applicationContext;
    }

}
