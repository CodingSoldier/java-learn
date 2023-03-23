package com.itheima.demo11;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 泛型与反射
 */
public class Test11 {
    public static void main(String[] args) throws Exception {
//        Class<Person> personClass = Person.class;
//        Constructor<Person> constructor = personClass.getConstructor();
//        Person person = constructor.newInstance();
//         Class personClass = Person.class;
//         Constructor constructor = personClass.getConstructor();
//         Object o = constructor.newInstance();

        Class<Person> personClass = Person.class;
        Constructor<Person> constructor = personClass.getConstructor();
        Person p = constructor.newInstance();

        Class clz = Person.class;
        Constructor con = clz.getConstructor();
        Object o = con.newInstance();

    }
}
