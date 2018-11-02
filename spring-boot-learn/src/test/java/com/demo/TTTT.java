package com.demo;

import org.junit.Test;

public class TTTT {
    @Test
    public void test1(){
        String name = "法定名称章（钢印）";
        String nameNew = name.substring(name.length() - 3, name.length()-1);
        System.out.println(MaterialUtil.getNameGangYin(name));
        System.out.println(MaterialUtil.getNameGangYin(name));
    }
}
