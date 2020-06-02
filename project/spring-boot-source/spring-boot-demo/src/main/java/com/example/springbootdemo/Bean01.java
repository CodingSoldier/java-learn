package com.example.springbootdemo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "enable.bean01", havingValue = "true")
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
