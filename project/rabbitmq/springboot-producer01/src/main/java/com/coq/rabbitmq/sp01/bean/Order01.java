package com.coq.rabbitmq.sp01.bean;

import java.io.Serializable;

public class Order01 implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    public Order01() {
    }

    public Order01(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MyOrder{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
