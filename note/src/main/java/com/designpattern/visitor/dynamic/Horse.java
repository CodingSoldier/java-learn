package com.designpattern.visitor.dynamic;

public class Horse {
    public void eat(){
        System.out.println("马吃草");
    }
}

class WhiteHorse extends Horse {
    @Override
    public void eat(){
        System.out.println("白马吃草");
    }
}

class BlackHorse extends Horse {
    @Override
    public void eat(){
        System.out.println("黑马吃草");
    }
}