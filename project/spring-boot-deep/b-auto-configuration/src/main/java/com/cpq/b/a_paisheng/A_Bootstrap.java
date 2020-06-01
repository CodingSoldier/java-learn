package com.cpq.b.a_paisheng;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.Annotation;
import java.util.Arrays;


/**
 * java原生不支持派生，spring注解支持派生性
 *
 * @Component
 * @Repository
 * @FirstLevelRepository 自定义注解也可以装配bean
 * @SecondLevelRepository 继续派生
 */
@ComponentScan("com.cpq.b.a_paisheng")
public class A_Bootstrap {
    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(A_Bootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        MyFirstBean mf = context.getBean(MyFirstBean.class);
        System.out.println("获取到bean " + mf);

        MySecondBean ms = context.getBean(MySecondBean.class);
        System.out.println("获取到bean " + ms);

        Annotation[] annotations = MySecondBean.class.getAnnotations();
        System.out.println(Arrays.toString(annotations));


        context.close();
    }
}
