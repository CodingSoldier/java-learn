package com.example.bspringboot.bean;

import lombok.Data;

@Data
public class Demo {
    private Long id;

    private String name;

    private String job;

    @Override
    public String toString() {
        return "Demo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                '}';
    }

}