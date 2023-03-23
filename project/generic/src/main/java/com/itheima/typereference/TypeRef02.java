package com.itheima.typereference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author chenpq05
 * @since 2023/3/23 14:00
 */
public abstract class TypeRef02<T> implements Comparable<TypeRef02<T>> {

    protected final Type _type;

    protected TypeRef02() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("非法参数");
        }
        _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return _type;
    }

    @Override
    public int compareTo(TypeRef02<T> o) {
        return 0;
    }

}
