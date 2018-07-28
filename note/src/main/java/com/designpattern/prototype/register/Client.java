package com.designpattern.prototype.register;

public class Client {
    public static void main(String[]args) throws Exception{
        Prototype p1 = new ConcretePrototype1();
        PrototypeManager.setPrototype("p1", p1);

        Prototype p11 = PrototypeManager.getPrototype("p1");
        p11.setName("张三");

        //输出 张三
        System.out.println(p1.getName());


        PrototypeManager.setPrototype("p1", new ConcretePrototype1());
        Prototype p111 = PrototypeManager.getPrototype("p1");
        System.out.println(p111.getName());


        Prototype p2 = PrototypeManager.getPrototype("p1").clone();
        p2.setName("222222");
        System.out.println(PrototypeManager.getPrototype("p1").getName());

        PrototypeManager.removePrototype("p1");
        PrototypeManager.getPrototype("p1").clone();
    }
}
