package com.designpattern.status;

public class Client {

    public static void main(String[] args){
        State state = new ConcreteStateA();
        Context context = new Context();
        context.setState(state);
        context.request("Test");
    }

}
