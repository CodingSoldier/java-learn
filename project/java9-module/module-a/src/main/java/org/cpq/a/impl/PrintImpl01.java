package org.cpq.a.impl;

import org.cpq.a.service.Print;

public class PrintImpl01 implements Print {

    // 第一个实现类默认的构造函数是无参构造函数
    @Override
    public void printMsg() {
        System.out.println("PrintImpl01");
    }

}
