package com.designpattern.bridgemode;

public class Abstraction {
    protected Implementor impl;
    public Abstraction(Implementor impl){
        this.impl = impl;
    }
    public void operation(){
        impl.operationImpl();
    }
}
