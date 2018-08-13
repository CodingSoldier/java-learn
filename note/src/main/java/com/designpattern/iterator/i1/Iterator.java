package com.designpattern.iterator.i1;

public interface Iterator {
    void first();
    void next();
    boolean isDone();
    Object currentItem();
}
