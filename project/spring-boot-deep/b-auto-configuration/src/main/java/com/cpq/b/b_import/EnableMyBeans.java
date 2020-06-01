package com.cpq.b.b_import;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
/**
 * 将MyBeansConfiguration导入IOC容器中
 */
@Import(MyBeansConfiguration.class)
public @interface EnableMyBeans {
}
