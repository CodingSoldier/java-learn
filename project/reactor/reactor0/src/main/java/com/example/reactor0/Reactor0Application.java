package com.example.reactor0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Reactor0Application {

    public static void main(String[] args) {
        SpringApplication.run(Reactor0Application.class, args);
        List<Thread> collect = Thread.getAllStackTraces()
                .keySet()
                .stream()
                .collect(Collectors.toList());
        System.out.println(collect.toString());
    }

}
