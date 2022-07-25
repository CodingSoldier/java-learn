package org.cpq.nettyinaction.a12_first_app.type;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TypeSub extends TypeParent<Integer, String> {
    public static void main(String[] args) throws Exception{
        // new TypeSub();

        System.out.println("##############");
        /**
         * 如果是继承基类而来的泛型，就用 getGenericSuperclass() , 转型为 ParameterizedType 来获得实际类型
         * 如果是实现接口而来的泛型，就用 getGenericInterfaces() , 针对其中的元素转型为 ParameterizedType 来获得实际类型
         */
        ParameterizedType parameterizedType = (ParameterizedType)TypeSub.class.getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        System.out.println("00000000000 " + actualTypeArguments[0]);
        System.out.println("11111   " + actualTypeArguments[1]);

    }
}

