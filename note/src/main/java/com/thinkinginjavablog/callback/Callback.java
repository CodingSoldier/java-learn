package com.thinkinginjavablog.callback;

/**
 * 定义一个接口，
 * new出匿名接口实例，在实例中覆盖callbackFn方法。
 */
public interface Callback<T> {

    void callbackFn(T t);

}
