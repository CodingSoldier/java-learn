package org.cpq.nettyinaction.a12_first_app.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TestAbs<T> implements TestInterface<T>{

    public void testObject(Object obj) {
        Type[] genericInterfaces = getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType)genericInterfaces[0];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.readValue("fsdfas", actualTypeArgument.getClass())
    }

    public abstract void testType(T type);
}
