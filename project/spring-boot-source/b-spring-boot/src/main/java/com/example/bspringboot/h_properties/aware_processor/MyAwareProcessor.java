package com.example.bspringboot.h_properties.aware_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MyAwareProcessor implements BeanPostProcessor {
    private ConfigurableApplicationContext cac;

    public MyAwareProcessor(ConfigurableApplicationContext cac) {
        this.cac = cac;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Aware){
            if (bean instanceof MyAware){
                ((MyAware)bean).setFlag((Flag)cac.getBean("flag"));
            }
        }
        return bean;
    }
}
