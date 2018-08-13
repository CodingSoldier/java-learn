package com.designpattern.command;

public class Invoker {
    private Command command = null;
    public Invoker(Command command){
        this.command = command;
    }
    public void action(){
        command.execute();
    }
}
