package com.cpq.cspringbootapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;


public class CSpringBootApplication {

    public static void main(String[] args) {
        /**
         * 简单方式启动工程，使用ApplicationConfiguration作为配置源
         */
        //SpringApplication.run(ApplicationConfiguration.class, args);


        /**
         * api方式启动工程，可以配置多个启动源
         */
        Set<String> sources = new HashSet<>();
        sources.add(ApplicationConfiguration.class.getName());
        SpringApplication application = new SpringApplication();
        application.setSources(sources);
        application.run(args);

    }

    /**
     * 加上了@SpringBootApplication的类都可以作为配置源
     */
    @SpringBootApplication
    public static class ApplicationConfiguration{

    }

}
