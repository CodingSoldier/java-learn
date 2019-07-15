package com.zs.diveinspringboot.externalizated.configuration.bootstrap;

import com.zs.diveinspringboot.externalizated.configuration.domain.User;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class XmlPlaceholderExternalizedConfigurationBootstrap {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(XmlPlaceholderExternalizedConfigurationBootstrap.class)
                .web(WebApplicationType.NONE)//非web应用
                .run(args);

        User user = applicationContext.getBean("user", User.class);

        System.out.println("用户对象: " + user);

        //关闭上下文
        applicationContext.close();
    }
}
