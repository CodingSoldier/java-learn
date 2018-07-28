package com.designpattern.prototype.simple;

public class Client {
    private Prototype prototype;

    public Client(Prototype prototype){
        this.prototype = prototype;
    }

    public void operation(Prototype example){
        Prototype copyPrototype = prototype.clone();
    }
}
