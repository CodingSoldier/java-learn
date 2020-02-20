package com.example.bspringboot.e_ioc.bean;

import org.springframework.stereotype.Component;

@Component
public class GetBeanTest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
