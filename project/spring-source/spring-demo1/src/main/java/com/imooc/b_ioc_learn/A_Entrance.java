package com.imooc.b_ioc_learn;

import com.imooc.a_controller.WelcomeController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenpiqian
 * @date: 2020-07-14
 */
@Configuration
@ComponentScan("com.imooc")
public class A_Entrance {

    public static void main(String[] args) {

        // 创建容器上下文 AnnotationConfigApplicationContext
        AnnotationConfigApplicationContext applicationcontext = new AnnotationConfigApplicationContext(A_Entrance.class);
        String[] beanDefinitionNames = applicationcontext.getBeanDefinitionNames();
        for (String name:beanDefinitionNames){
            System.out.println(name);
        }
        // 通过容器获取bean
        WelcomeController welcomeController = (WelcomeController)applicationcontext.getBean("welcomeController");
        System.out.println(welcomeController);
    }

}
