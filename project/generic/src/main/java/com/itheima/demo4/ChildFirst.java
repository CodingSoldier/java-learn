package com.itheima.demo4;

/**
 * 泛型类派生子类，子类也是泛型类，那么子类的泛型标识要和父类一致。
 * @param <T>
 */
public class ChildFirst<T> extends Parent<T> {
    @Override
    public T getValue() {
        return super.getValue();
    }
}
