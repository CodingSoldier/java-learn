package com.designpattern.iterator;

public interface Iterator {
    void first();
    void next();
    boolean isDone();
    Object currentItem();
}
