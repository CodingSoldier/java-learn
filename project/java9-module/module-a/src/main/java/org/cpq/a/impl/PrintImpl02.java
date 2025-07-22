package org.cpq.a.impl;

import org.cpq.a.service.Print;

public class PrintImpl02 implements Print {

    private String name;

    public PrintImpl02(String name) {
        this.name = name;
    }

    public static Print provider() {
        return new PrintImpl02("实现类没有无参构造函数，则规定使用public static Print provider()返回PrintImpl02实例");
    }

    @Override
    public void printMsg() {
        System.out.println("PrintImpl02  ： " + this.name);
    }

}
