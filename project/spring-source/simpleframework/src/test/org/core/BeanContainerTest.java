package org.core;


import com.c.service.solo.HeadLineService;
import org.junit.jupiter.api.*;

import java.util.Set;

/**
 * @author chenpiqian
 * @date: 2020-07-13
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeanContainerTest {

    private static BeanContainer beanContainer;

    @BeforeAll
    static void init(){
        beanContainer = BeanContainer.getInstance();
    }

    @DisplayName("加载目标类及其实例到BeanContainer：loadBeansTest")
    @Order(1)
    @Test
    public void loadBeansTest(){
        beanContainer.loadBeans("com.c");
    }

    @Order(4)
    @Test
    public void getClassesBySuperTest(){
        beanContainer.loadBeans("com.c");
        Set<Class<?>> classesBySuper = beanContainer.getClassesBySuper(HeadLineService.class);
        System.out.println(classesBySuper.toString());
    }

}
