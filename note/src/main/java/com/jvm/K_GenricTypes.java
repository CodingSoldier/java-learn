package com.jvm;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class K_GenricTypes {
    public static void method(List<String> list) {
        System.out.println("method");
    }
}

class K_GenricTypesMain {

    public static void main(String[] args) throws Exception{
        Method method = K_GenricTypes.class.getMethod("method",List.class);
        Type[] types = method.getGenericParameterTypes();
        for (Type type : types) {
            System.out.println("参数类型："+type);
            //参数类型：java.util.List<java.lang.String>
            if(type instanceof ParameterizedType){
                Type[] paramType = ((ParameterizedType) type).getActualTypeArguments();
                for (Type t : paramType) {
                    System.out.println("泛型类型："+t);
                    //泛型类型：class java.lang.String
                }
            }
        }

    }
}
