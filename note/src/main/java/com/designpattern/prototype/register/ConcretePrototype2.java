package com.designpattern.prototype.register;

public class ConcretePrototype2 implements Prototype {
    private String name;

    @Override
    public Prototype clone() {
        ConcretePrototype2 prototype2 = new ConcretePrototype2();
        prototype2.setName(this.name);
        return prototype2;
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
