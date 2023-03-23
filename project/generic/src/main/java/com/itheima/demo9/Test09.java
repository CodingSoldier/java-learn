package com.itheima.demo9;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 泛型擦除
 */
public class Test09 {
    public static void main(String[] args) {
//        ArrayList<Integer> intList = new ArrayList<>();
//        ArrayList<String> strList = new ArrayList<>();
//
//        System.out.println(intList.getClass().getSimpleName());
//        System.out.println(strList.getClass().getSimpleName());
//
//        System.out.println(intList.getClass() == strList.getClass());
        Erasure<Integer> erasure = new Erasure<>();
        //利用反射，获取Erasure类的字节码文件的Class类对象
        Class<? extends Erasure> clz = erasure.getClass();
        //获取所有的成员变量
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            //打印成员变量的名称和类型
            System.out.println(declaredField.getName() + ":" + declaredField.getType().getSimpleName());
        }
        System.out.println("----------------------------------------------------------------");
        //获取所有的方法
        Method[] declaredMethods = clz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            //打印方法名和方法的返回值类型。
            System.out.println(declaredMethod.getName() + ":" + declaredMethod.getReturnType().getSimpleName());
        }
        System.out.println("----------------------------------------------------------------");
        Class<InfoImpl> infoClass = InfoImpl.class;
        //获取所有的方法
        Method[] infoImplMethods = infoClass.getDeclaredMethods();
        for (Method method : infoImplMethods) {
            //打印方法名和方法的返回值类型。
            System.out.println(method.getName() + ":" + method.getReturnType().getSimpleName());
        }
    }
}
