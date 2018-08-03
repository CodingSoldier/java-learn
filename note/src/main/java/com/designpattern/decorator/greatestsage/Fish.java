package com.designpattern.decorator.greatestsage;

public class Fish extends Change {

    public Fish(TheGreatestSage sage) {
        super(sage);
    }

    @Override
    public void move() {
        System.out.println("fish move");
    }

}
