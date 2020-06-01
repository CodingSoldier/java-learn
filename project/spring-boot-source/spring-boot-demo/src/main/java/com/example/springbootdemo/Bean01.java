package com.example.springbootdemo;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-08
 */
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
