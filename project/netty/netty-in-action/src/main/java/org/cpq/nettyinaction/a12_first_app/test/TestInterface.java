package org.cpq.nettyinaction.a12_first_app.test;

public interface TestInterface<T> {

    void testObject(Object obj);

    void testType(T type);
}
