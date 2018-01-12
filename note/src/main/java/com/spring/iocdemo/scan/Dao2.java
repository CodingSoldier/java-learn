package com.spring.iocdemo.scan;

import com.spring.iocdemo.ComponentCustom;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/13
 */
//@ComponentCustom(name = "dao2")
//@ComponentCustom("dao2")
@ComponentCustom
public class Dao2 {
    private String name2;
    public void show(){
        System.out.println("Dao2的show方法*******2222****");
    }
}
