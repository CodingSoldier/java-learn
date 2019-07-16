package com.example.cpq.hconfiguration02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@EnableAutoConfiguration
public class C_Environment {

    // @Autowired注入Environment
    @Autowired
    Environment environment;


    @Bean
    public User02 user02(){
        Long id = environment.getRequiredProperty("user.id", Long.class);
        String name = environment.getRequiredProperty("user.name", String.class);
        Integer ageNew = environment.getProperty("user.ageNew", Integer.class, 100);

        User02 user02 = new User02();
        user02.setId(id);
        user02.setName(name);
        user02.setAge(ageNew);
        return user02;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(C_Environment.class)
                .web(WebApplicationType.NONE)
                .run(args);

        User02 user02 = app.getBean( User02.class);
        System.err.println("用户bean"+user02);


        app.close();
    }

}
