package com.designpattern.adapter;

public class Adapter extends Adaptee implements Target {
    @Override
    public void sampleOperation02() {
        System.out.println("实现sampleOperation02");
    }
}
