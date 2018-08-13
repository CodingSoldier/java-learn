package com.designpattern.memento.p2;

import java.util.ArrayList;
import java.util.List;

public class Originator {
    private List<String> states;
    private int index;
    public Originator(){
        states=new ArrayList<>();
        index=0;
    }
    public Memento createMemento(){
        return new Memento(states, index);
    }
    public void restoreMemento(Memento memento){
        states=memento.getStates();
        index=memento.getIndex();
    }
    public void setState(String state){
        states.add(state);
        index++;
    }
    public void printStates(){
        for (String state:states){
            System.out.println(state);
        }
    }
}
