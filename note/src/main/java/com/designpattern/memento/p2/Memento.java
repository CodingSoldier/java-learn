package com.designpattern.memento.p2;

import java.util.ArrayList;
import java.util.List;

public class Memento {
    private List<String> states;
    private int index;
    public Memento(List<String> states, int index){
        this.states = new ArrayList<>(states);
        this.index = index;
    }
    public List<String> getStates(){
        return states;
    }
    public int getIndex(){
        return index;
    }
}
