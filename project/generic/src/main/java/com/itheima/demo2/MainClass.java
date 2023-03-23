package com.itheima.demo2;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * 泛型类
 */
public class MainClass {
    public static void main(String[] args) {
        // //泛型类在创建对象的时候，来指定操作的具体数据类型。
        Generic<String> strGeneric = new Generic<>("abc");
        // String key1 = strGeneric.getKey();
        // System.out.println("key1:" + key1);
        //
        // System.out.println("-----------------------------------");
        // Generic<Integer> intGeneric = new Generic<>(100);
        // int key2 = intGeneric.getKey();
        // System.out.println("key2:" + key2);
        //
        // System.out.println("-----------------------------------");
        // //泛型类在创建对象的时候，没有指定类型，将按照Object类型来操作。
        // Generic generic = new Generic("ABC");
        // Object key3 = generic.getKey();
        // System.out.println("key3:" + key3);
        //
        // //泛型类，不支持基本数据类型。
        // //Generic<int> generic1 = new Generic<int>(100);
        //
        // System.out.println("-----------------------------------");
        // //同一泛型类，根据不同的数据类型创建的对象，本质上是同一类型。
        // System.out.println(intGeneric.getClass());
        // System.out.println(strGeneric.getClass());
        // System.out.println(intGeneric.getClass() == strGeneric.getClass());


        Type genericSuperclass = strGeneric.getClass().getGenericSuperclass();
        System.out.println(genericSuperclass.getTypeName());


        System.out.println( genericSuperclass instanceof ParameterizedType); // true
        System.out.println( genericSuperclass instanceof Class); // false
        System.out.println( genericSuperclass instanceof WildcardType); // false
        System.out.println( genericSuperclass instanceof GenericArrayType); // false

        // ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        // System.out.println(parameterizedType.getActualTypeArguments()[0]);
    }
}
