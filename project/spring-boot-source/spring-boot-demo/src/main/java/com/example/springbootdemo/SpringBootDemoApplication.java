package com.example.springbootdemo;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@EnableMyBeans
//@EnableMyBeans(enableMyImportSelector = false)
public class SpringBootDemoApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SpringBootDemoApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        MyBeansConfiguration myBeansConfiguration = context.getBean(MyBeansConfiguration.class);
        System.out.println("获取myBeansConfiguration: "+myBeansConfiguration);

        Bean01 bean01 = context.getBean(Bean01.class);
        System.out.println("获取到bean " + bean01);

        context.close();
    }
}
