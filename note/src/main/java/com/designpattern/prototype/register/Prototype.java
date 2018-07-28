package com.designpattern.prototype.register;

public interface Prototype {
    Prototype clone();
    String getName();
    void setName(String name);
}
