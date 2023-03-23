package com.itheima.demo1;

import java.util.ArrayList;

/**
 * 泛型产生的背景
 */
public class MainClass {
    public static void main(String[] args) {
//        ArrayList list = new ArrayList();
//        list.add("java");
//        list.add(100);
//        list.add(true);
//
//        for (int i = 0; i < list.size(); i++) {
//            Object o = list.get(i);
//            String str = (String)o;
//            System.out.println(str);
//        }

        // //泛型：
        // // 编译期间检查类型
        // //减少了数据类型转换
        // ArrayList<String> strList = new ArrayList<>();
        // strList.add("a");
        // strList.add("b");
        // strList.add("c");

        // for (int i = 0; i < strList.size(); i++) {
        //     String s = strList.get(i);
        //     System.out.println(s);
        // }

        System.out.println("------------------------------------");
        ArrayList<Integer> intList = new ArrayList<>();
        intList.add(100);
        intList.add(200);
        intList.add(300);

        for (int i = 0; i < intList.size(); i++) {
            int num = intList.get(i);
            System.out.println(num);
        }
    }
}