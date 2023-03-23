package com.itheima.demo10;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 泛型与数组
 */
public class Test10 {
    public static void main(String[] args) {
//        ArrayList[] list = new ArrayList[5];
//        ArrayList<String>[] listArr = list;
        ArrayList<String>[] listArr = new ArrayList[5];

        ArrayList<String>[] arrayListArr = new ArrayList[5];

        ArrayList<Integer> intList = new ArrayList();
        intList.add(100);

        ArrayList<String> strList = new ArrayList();
        strList.add("abc");

        //listArr[0] = intList;
        listArr[0] = strList;
        //list[0] = intList;
        String s = listArr[0].get(0);
        System.out.println(s);
        System.out.println("-------------------------------------------------------");
        Fruit<String> fruit = new Fruit<>(String.class,3);
        fruit.put(0,"苹果");
        fruit.put(1,"西瓜");
        fruit.put(2,"香蕉");

        System.out.println(Arrays.toString(fruit.getArray()));
        String s1 = fruit.get(2);
        System.out.println(s1);
    }
}
