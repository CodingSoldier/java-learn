package com.spring;

import org.junit.Test;

import java.lang.reflect.Field;

class MyBean {
    private String message;

    public void showMsg(){
        System.out.println(message);
    }
}

public class GetSetValue {
    @Test
    public void t() throws Exception{
        MyBean instance = new MyBean();
        setValue(instance, "message", "Shht! Don't tell anyone!");
        instance.showMsg();
        System.out.println("The message is '" + getValue(instance, "message"));
    }

    private static void setValue(Object bean, String fieldName, Object value)
            throws IllegalArgumentException, IllegalAccessException,
            SecurityException, NoSuchFieldException {
        Field privateVar = bean.getClass().getDeclaredField(fieldName);
        privateVar.setAccessible(true);
        privateVar.set(bean, value);
    }
    private static Object getValue(Object bean, String fieldName)
            throws IllegalArgumentException, IllegalAccessException,
            SecurityException, NoSuchFieldException {
        Field privateVar = bean.getClass().getDeclaredField(fieldName);
        privateVar.setAccessible(true);
        return privateVar.get(bean);
    }
}
