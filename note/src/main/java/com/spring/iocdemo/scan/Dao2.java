package com.spring.iocdemo.scan;

import com.spring.iocdemo.ComponentCustom;

/**
 * @Author：cpq
 * @Description： 通过注解加入到IOC容器中
 */
//@ComponentCustom(name = "dao222")
//@ComponentCustom("dao22")
@ComponentCustom
public class Dao2 {
    private String name2;
    public void show(){
        System.out.println("Dao2的show方法*******2222****");
    }
}
