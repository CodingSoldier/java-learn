package com.example.imagetest.hpa;

public class MemObj {
    private String name;
    private Integer num;

    public MemObj() {

    }

    public MemObj(String name, Integer num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
