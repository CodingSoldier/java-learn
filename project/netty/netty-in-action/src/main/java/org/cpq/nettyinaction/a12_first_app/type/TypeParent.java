package org.cpq.nettyinaction.a12_first_app.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeParent<T, R> {

    private Class<T> mTClass;
    private Class<R> mRClass;

    @SuppressWarnings("unchecked")
    public TypeParent() {
        Type t = getClass().getGenericSuperclass();
        System.out.println("getClass() == " + getClass());
        System.out.println("getClass().getSuperclass() == " + getClass().getSuperclass());
        System.out.println("getClass().getGenericSuperclass() == " + t);
        Type firType = ((ParameterizedType) t).getActualTypeArguments()[0];
        System.out.println("((ParameterizedType) t).getActualTypeArguments()[0] == " + firType);
        Type secType = ((ParameterizedType) t).getActualTypeArguments()[1];
        System.out.println("((ParameterizedType) t).getActualTypeArguments()[1] == " + secType);
        try {
            mTClass = (Class<T>) firType;
            mRClass = (Class<R>) secType;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        System.out.println("getActualTypeArguments() length == " + ((ParameterizedType) t).getActualTypeArguments().length);
    }
}
