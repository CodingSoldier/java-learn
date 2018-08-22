package com.designpattern.visitor.dynamic;

public class Mozi {

    public static void main(String[] args) {
        Horse wh = new WhiteHorse();
        Horse bh = new BlackHorse();
        wh.eat();
        bh.eat();
    }

}
