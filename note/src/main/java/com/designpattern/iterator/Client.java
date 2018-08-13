package com.designpattern.iterator;

public class Client {
    public static void main(String[] args) {
        Object[] objArray = {"One", "Two", "Three", "Four", "Five"};
        Aggregate agg = new ConcreteAggregate(objArray);
        Iterator it = agg.createIterator();
        while (!it.isDone()){
            System.out.println(it.currentItem());
            it.next();
        }
    }
}
