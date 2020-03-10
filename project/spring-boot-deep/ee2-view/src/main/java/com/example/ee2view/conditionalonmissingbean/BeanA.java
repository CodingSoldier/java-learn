package com.example.ee2view.conditionalonmissingbean;

/**
 * @author chenpiqian
 * @date: 2020-03-10
 */

public class BeanA {

    public BeanA(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
