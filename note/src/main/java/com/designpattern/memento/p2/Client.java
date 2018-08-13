package com.designpattern.memento.p2;

public class Client {
    public static void main(String[] args) {
        Originator o = new Originator();
        Caretaker c = new Caretaker(o);

        o.setState("state 0");
        c.createMemento();

        o.setState("state 1");
        o.createMemento();

        o.setState("state 2");
        o.createMemento();

        o.setState("state 3");
        o.createMemento();

        o.printStates();
        System.out.println("----------恢复检查点-----------");

        c.restoreMemento(2);
        o.printStates();
    }
}
