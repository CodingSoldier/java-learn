package com.example.cpq.hconfiguration02;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@EnableAutoConfiguration
public class F_Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext app =
                new SpringApplicationBuilder(F_Main.class)
                .web(WebApplicationType.NONE)
                .run(args);

        ConfigurableEnvironment environment = app.getEnvironment();
        System.out.println("环境变量："+environment.getProperty("user.city.name"));

        app.close();
    }

}
