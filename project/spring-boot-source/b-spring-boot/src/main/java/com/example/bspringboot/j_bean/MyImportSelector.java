package com.example.bspringboot.j_bean;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author chenpiqian
 * @date: 2020-02-24
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        System.out.println("~~~~~~~~~MyImportSelector");
        return new String[]{"com.example.bspringboot.e_ioc.bean.Bird", "com.example.bspringboot.e_ioc.bean.Teacher"};
    }
}
