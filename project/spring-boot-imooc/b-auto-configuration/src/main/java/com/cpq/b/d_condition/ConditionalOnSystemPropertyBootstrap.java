package com.cpq.b.d_condition;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-08
 */
public class ConditionalOnSystemPropertyBootstrap {

    /**
     * com.cpq.b.d_condition.OnSystemPropertyCondition#matches()条件满足，则装配bean
     * @return
     */
    @Bean
    @ConditionalOnSystemProperty(name = "user.name", value = "Administrator")
    public String helloWorld(){
        return "helloWorld!";
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ConditionalOnSystemPropertyBootstrap.class)
                .web(WebApplicationType.NONE)
                .run(args);

        String helloWorld = context.getBean("helloWorld", String.class);

        System.out.println("helloWorld Bean : " + helloWorld);

        // 关闭上下文
        context.close();
    }
}
