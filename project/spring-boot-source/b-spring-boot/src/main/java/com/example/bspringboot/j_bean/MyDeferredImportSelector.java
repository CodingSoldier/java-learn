package com.example.bspringboot.j_bean;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 启动类加上 @Import({MyDeferredImportSelector.class, MyImportSelector.class})
 */
public class MyDeferredImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        System.out.println("~~~~~~~~~MyDeferredImportSelector");
        return new String[]{"com.example.bspringboot.e_ioc.bean.Cat", "com.example.bspringboot.e_ioc.bean.Dog"};
    }
}
