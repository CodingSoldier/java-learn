package com.learn.d_refresh_listener;

import org.springframework.core.ResolvableType;

import java.util.HashMap;
import java.util.List;

public class B_ResolvebleDemo {

    private HashMap<String, List<Integer>> customizedMap;

    /**
     * ResolvableType可以获取Class、Filed、Method的泛型
     */
    public static void main(String[] args) throws NoSuchFieldException {
        ResolvableType resolvableType = ResolvableType.forField(B_ResolvebleDemo.class.getDeclaredField("customizedMap"));
        System.out.println("######resolvableType.getGeneric(0).resolve()    "+resolvableType.getGeneric(0).resolve());
        System.out.println("######resolvableType.getGeneric(1).resolve()    "+resolvableType.getGeneric(1).resolve());
        System.out.println("######resolvableType.getGeneric(1)    "+resolvableType.getGeneric(1));
        System.out.println("######resolvableType.getSuperType()    "+resolvableType.getSuperType());
        System.out.println("######resolvableType.asMap()    "+resolvableType.asMap());
        System.out.println("######resolvableType.resolveGeneric(1,0)    "+resolvableType.resolveGeneric(1,0));
    }

    /**
     * com.imooc.b_ioc_learn.D_AbstractApplicationContext#refresh() 是重点
     */

}
