package com.itheima.typereference;

import com.itheima.demo11.Person;

import java.lang.reflect.ParameterizedType;

/**
 * @author chenpq05
 * @since 2023/3/23 10:49
 */
abstract class TypeRef<T> {

    public Class<T> getGenericClass() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    public static void main(String[] args) {
        TypeRef<Person> superClass = new TypeRef<Person>() {
        };
        System.out.println(superClass.getGenericClass());
    }

}
