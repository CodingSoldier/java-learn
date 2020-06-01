package com.example.springbootdemo;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-08
 */
public class MyImportSelector implements ImportSelector{

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        Map<String, Object> enableMyBeansAttributes = annotationMetadata.getAnnotationAttributes(EnableMyBeans.class.getName());
        System.out.println("EnableMyBeans属性执行自定义逻辑，比方说不将MyBeansConfiguration注入到IOC容器中");
        boolean enableMyImportSelector = Boolean.parseBoolean(String.valueOf(enableMyBeansAttributes.get("enableMyImportSelector")));
        return enableMyImportSelector ? new String[]{MyBeansConfiguration.class.getName()} : new String[]{};
    }

}
