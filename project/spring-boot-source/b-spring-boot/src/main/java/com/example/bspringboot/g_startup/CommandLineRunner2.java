package com.example.bspringboot.g_startup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class CommandLineRunner2 implements CommandLineRunner {

    @Value("${key1}")
    String key1;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("#######启动加载器--CommandLineRunner2  "+key1);
        System.out.println("#######启动加载器--CommandLineRunner2");
    }
}
