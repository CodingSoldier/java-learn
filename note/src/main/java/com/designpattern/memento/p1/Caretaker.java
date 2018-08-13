package com.designpattern.memento.p1;

public class Caretaker {
    private MementoIF memento;
    public MementoIF retrieveMemento(){
        return memento;
    }
    public void saveMemento(MementoIF memento){
        this.memento=memento;
    }
}
