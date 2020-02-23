package com.example.bspringboot.h_properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Value("${property.key}")
    String propertyKey;
    @Value("${property.random}")
    String propertyRandom;
    @Value("${property.vm.name}")
    String propertyvmname;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("$$$$$  "+propertyKey);
        System.out.println("$$$$$  "+propertyRandom);
        System.out.println("$$$$$  "+propertyvmname);

        System.out.println("$$$MyEnvironmentAware.getPropertyKey()："+MyEnvironmentAware.getPropertyKey());
    }
}
