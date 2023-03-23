package com.itheima.typereference;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author chenpq05
 * @since 2023/3/23 13:57
 */
public class Test01 {

    public static void main(String[] args) {

        // new TypeReference<>()

        TypeRef02<Map<Integer, String>> typeRef02 = new TypeRef02<Map<Integer, String>>() {
        };

        Type type02 = typeRef02.getType();
        System.out.println(type02);

        ParameterizedType parameterizedType02 = (ParameterizedType) typeRef02.getClass().getGenericSuperclass();
        System.out.println(parameterizedType02);

        ParameterizedType parameterizedTypeMap = (ParameterizedType) parameterizedType02.getActualTypeArguments()[0];
        System.out.println(parameterizedTypeMap);

        Class clz1 = (Class) parameterizedTypeMap.getActualTypeArguments()[0];
        Class clz2 = (Class) parameterizedTypeMap.getActualTypeArguments()[1];
        System.out.println(clz1);
        System.out.println(clz2);

        System.out.println("");

    }

}
