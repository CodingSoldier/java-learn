package com.example.cpq.hconfiguration02;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class D_Environment {

    //使用前缀
    @ConfigurationProperties(prefix = "user")
    @Bean
    public User03 user03(){
        return new User03();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(D_Environment.class)
                .web(WebApplicationType.NONE)
                .run(args);

        User03 user03 = app.getBean( User03.class);
        System.err.println("用户bean"+user03);


        app.close();
    }

}
