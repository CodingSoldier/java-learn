package com.example.cpq.hconfiguration;

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
public class B_XmlConfig {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(B_XmlConfig.class)
                .web(WebApplicationType.NONE)
                .run(args);

        // 系统属性配置覆盖了application.properties中的配置
        //运行参数加上 --user.name=cpq ，user.name显示cpq。优先级：命令行参数 > 系统配置 > application.properties
        User user = app.getBean("user", User.class);
        System.err.println("用户bean"+user);

        System.err.println("系统属性配置，user.name=" + System.getProperty("user.name"));

        app.close();
    }

}
