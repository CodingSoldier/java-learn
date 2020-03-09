package com.example.cpq.hconfiguration02;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
    注解方式读取xml配置
 */
@ImportResource("META-INF/spring/user-context.xml")
@EnableAutoConfiguration
public class A_XmlConfig02 {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(A_XmlConfig02.class)
                .web(WebApplicationType.NONE)
                .run(args);

        User user = app.getBean("user", User.class);
        System.err.println("用户bean"+user);

        System.err.println("系统属性配置，user.name=" + System.getProperty("user.name"));

        app.close();
    }

}
