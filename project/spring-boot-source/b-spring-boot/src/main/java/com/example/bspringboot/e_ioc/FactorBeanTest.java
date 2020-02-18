package com.example.bspringboot.e_ioc;

import com.example.bspringboot.e_ioc.bean.Animal;
import com.example.bspringboot.e_ioc.bean.Bird;
import com.example.bspringboot.e_ioc.bean.Dog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(BeanDefinitionRegistrar.class)
class FactorBeanTest {

    @Autowired
    //@Qualifier("dog")
    @Qualifier("factorBeanIoc")
    Animal animal;

    @Autowired
    Dog dog;

    @Autowired
    Bird bird;

    @Test
    void test01() {
        System.out.println("动物名称  " + animal.getName());
        System.out.println("动物名称  " + dog.getName());
        System.out.println("动物名称  " + bird.getName());
    }

}
