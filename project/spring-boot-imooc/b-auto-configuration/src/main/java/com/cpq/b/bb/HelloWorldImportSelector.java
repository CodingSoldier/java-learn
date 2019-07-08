package com.cpq.b.bb;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-08
 */
public class HelloWorldImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{BB_HelloWorldConfiguration.class.getName()};
    }
}
