package com.example.bspringboot.e_ioc;

import com.example.bspringboot.e_ioc.bean.Animal;
import com.example.bspringboot.e_ioc.bean.Cat;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 实现FactoryBean方式注入Bean
   注入方式：
   @Autowired
   Animal animal;

 */
@Component
public class FactorBeanIoc implements FactoryBean<Animal> {

    @Override
    public Animal getObject() {
        return new Cat();
    }

    @Override
    public Class<?> getObjectType() {
        return Animal.class;
    }

}
