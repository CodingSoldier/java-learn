package com.example.ee2view.conditionalonmissingbean;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test01 {

    @Autowired
    BeanA beanA;
    @Autowired
    BeanA beanA2;

    @Test
    public void contextLoads() {
        System.out.println("###########"+beanA.getName());
        System.out.println("###########"+beanA2.getName());
    }

}
