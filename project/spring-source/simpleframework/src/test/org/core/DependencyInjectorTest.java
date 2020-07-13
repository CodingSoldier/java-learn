package org.core;

import com.c.controller.frontend.MainPageController;
import org.core.inject.DependencyInjector;
import org.junit.jupiter.api.Test;

/**
 * @author chenpiqian
 * @date: 2020-07-13
 */
public class DependencyInjectorTest {

    @Test
    public void doIocTest(){
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("com.c");
        new DependencyInjector().doIoc();
        MainPageController mainPageController = (MainPageController) beanContainer.getBean(MainPageController.class);
        System.out.println(mainPageController);
    }

}
