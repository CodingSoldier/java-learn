package com.cpq.b.e_auto;

import com.cpq.b.b_import.Bean01;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
public class BootstrapEnableAutoConfiguration {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(BootstrapEnableAutoConfiguration.class)
                .web(WebApplicationType.NONE)
                .run(args);

        String helloWorld = context.getBean("helloWorld",String.class);
        System.out.println("获取到bean " + helloWorld);

        Bean01 bean01 =  context.getBean("bean01", Bean01.class);
        System.out.println("获取bean01   "+ bean01.toString());

        // 关闭上下文
        context.close();

    }
}
