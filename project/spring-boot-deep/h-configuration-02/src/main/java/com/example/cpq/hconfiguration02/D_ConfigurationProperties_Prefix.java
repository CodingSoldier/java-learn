package com.example.cpq.hconfiguration02;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class D_ConfigurationProperties_Prefix {

    @Bean
    public User03 user03(){
        return new User03();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(D_ConfigurationProperties_Prefix.class)
                .web(WebApplicationType.NONE)
                .run(args);

        User03 user03 = app.getBean(User03.class);
        System.err.println("用户bean"+user03);
        app.close();
    }

}
