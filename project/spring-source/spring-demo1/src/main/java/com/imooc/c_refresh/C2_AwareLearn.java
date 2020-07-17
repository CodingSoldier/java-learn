package com.imooc.c_refresh;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.imooc.c_refresh")
public class C2_AwareLearn {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext app = new AnnotationConfigApplicationContext(A1_PostProcessor.class);
        C1_Aware c1_aware = (C1_Aware)app.getBean("c1_Aware");
        c1_aware.testAware();
    }

}
