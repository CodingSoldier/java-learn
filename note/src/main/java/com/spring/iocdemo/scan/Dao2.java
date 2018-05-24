package com.spring.iocdemo.scan;

import com.spring.iocdemo.ComponentCustom;

//@ComponentCustom(name = "dao222")
//@ComponentCustom("dao22")
@ComponentCustom
public class Dao2 {
    private String name2;
    public void show(){
        System.out.println("Dao2的show方法*******2222****");
    }
}
