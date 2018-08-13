package com.designpattern.memento;

public class Client {

    public static void main(String[] args) {
        Originator o = new Originator();
        Caretaker c = new Caretaker();
        o.setState("On");

        c.saveMemento(o.createMemento());

        o.setState("Off");
        o.restoreMemento(c.retrieveMemento());

        System.out.println(o.getState());
    }

}
