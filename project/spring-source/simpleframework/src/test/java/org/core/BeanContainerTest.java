package org.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanContainerTest {

    private static BeanContainer beanContainer;

    @BeforeAll
    static void init(){
        beanContainer = BeanContainer.getInstance();
    }

    @Test
    @DisplayName("加载目标类及其实例到BeanContainer")
    public void loadBeansTest(){
        Assertions.assertEquals(false, beanContainer.isLoaded());
        beanContainer.loadBeans("com.b");
        Assertions.assertEquals(true, beanContainer.isLoaded());
        System.out.println(beanContainer.size());
        System.out.println(beanContainer.toString());
    }

}