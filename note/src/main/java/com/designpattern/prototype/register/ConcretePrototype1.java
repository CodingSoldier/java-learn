package com.designpattern.prototype.register;

public class ConcretePrototype1 implements Prototype {
    private String name;

    @Override
    public Prototype clone() {
        ConcretePrototype1 prototype1 = new ConcretePrototype1();
        prototype1.setName(this.name);
        return prototype1;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
