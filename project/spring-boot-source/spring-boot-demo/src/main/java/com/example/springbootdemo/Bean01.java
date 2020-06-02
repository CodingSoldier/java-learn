package com.example.springbootdemo;

import org.springframework.stereotype.Component;

@Component
@ConditionalOnSystemAndProperty(name = "enable.bean01", value = "true")
public class Bean01 {
    private String name;
    public Bean01() {
    }
    public Bean01(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Bean01{" +
                "name='" + name + '\'' +
                '}';
    }
}
