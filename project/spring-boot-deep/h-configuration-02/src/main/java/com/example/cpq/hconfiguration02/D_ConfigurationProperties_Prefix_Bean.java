package com.example.cpq.hconfiguration02;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class D_ConfigurationProperties_Prefix_Bean {

    // @Bean上使用前缀
    @ConfigurationProperties(prefix = "user")
    @Bean
    public User04 user04(){
        return new User04();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext app =
                new SpringApplicationBuilder(D_ConfigurationProperties_Prefix_Bean.class)
                .web(WebApplicationType.NONE)
                .run(args);

        User04 user04 = app.getBean(User04.class);
        System.err.println("用户user04  "+user04);

        app.close();
    }

}
