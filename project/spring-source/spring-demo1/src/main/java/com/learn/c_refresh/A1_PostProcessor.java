package com.learn.c_refresh;

import com.learn.b_ioc_learn.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.learn.c_refresh")
public class A1_PostProcessor {

/**
 后置处理器PostProcessor，本身也是一种需要注册到容器里的Bean
    其里面的方法会在特定的时机被容器调用
    实现不改变容器或者Bean核心逻辑的情况下对Bean进行拓展
    对Bean进行包装，影响其行为、修改Bean的内容

 PostProcessor大致分为容器级别后置处理器以及Bean级别的后置处理器
    BeanDefinitionRegistryPostProcessor
    BeanFactoryPostProcessor
    BeanPostProcessor

 */
    public static void main(String[] args) {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(A1_PostProcessor.class);
        User user5 = (User)app.getBean("user5");
        System.out.println("##########"+user5);
    }

}
