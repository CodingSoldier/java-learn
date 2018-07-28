package com.designpattern.builder;

public class Director {
    private Builder builder;

    public Director(Builder builder){
        this.builder = builder;
    }

    public void construct(){
        builder.builderPart1();
        builder.builderPart2();
    }
}
