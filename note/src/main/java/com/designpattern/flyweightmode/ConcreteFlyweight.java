package com.designpattern.flyweightmode;

public class ConcreteFlyweight implements Flyweight {

    private Character intrinsicState = null;

    public ConcreteFlyweight(Character intrinsicState) {
        this.intrinsicState = intrinsicState;
    }

    @Override
    public void operation(String state) {
        System.out.println("Instrinsic State = "+this.intrinsicState);
        System.out.println("Extrinsic State = " + state);
    }
}
