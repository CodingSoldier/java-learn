package com.designpattern.decorator.greatestsage;

public class Bird extends Change {
    public Bird(TheGreatestSage sage){
        super(sage);
    }

    public void move(){
        System.out.println("bird move");
    }
}
