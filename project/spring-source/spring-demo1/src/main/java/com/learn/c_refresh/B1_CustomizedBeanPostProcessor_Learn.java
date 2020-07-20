package com.learn.c_refresh;

import com.learn.b_ioc_learn.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.learn")
public class B1_CustomizedBeanPostProcessor_Learn {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(A1_PostProcessor.class);
        User user5 = (User)app.getBean("user5");
        System.out.println("##########"+user5);
    }

}
