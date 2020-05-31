package com.example.cpq.hconfiguration02;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class E_ConditionalOnProperty {

    /**
     * @ConditionalOnProperty 通过属性配置条件
     * havingValue：如果 name/value 的值与 havingValue 相等，则满足条件
     * 一般与@Bean配合，组成条件装配
     *
     * @ConfigurationProperties 读取属性
     */
    @ConditionalOnProperty(prefix = "user", name = "enable", havingValue = "true")
    //@ConditionalOnProperty(prefix = "user", name = "enablexxx", havingValue = "true")
    //@ConditionalOnProperty(name = "user.enable", havingValue = "true")
    @Bean
    @ConfigurationProperties(prefix = "user")
    public User04 user04(){
        return new User04();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext app =
                new SpringApplicationBuilder(E_ConditionalOnProperty.class)
                .web(WebApplicationType.NONE)
                .run(args);

        //User04 user04 = app.getBean(User04.class);
        //System.err.println("用户user04  "+user04);

        app.close();
    }

}
