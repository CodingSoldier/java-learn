package com.designpattern.flyweightmode.complex;

public class ConcreteFlyweight implements Flyweight {
    private Character intrinsicState = null;
    public ConcreteFlyweight(Character state){
        this.intrinsicState = state;
    }
    public void operation(String state){
        System.out.println("intrinsic state = "+this.intrinsicState);
        System.out.println("extrinsic state = "+state);
    }
}
