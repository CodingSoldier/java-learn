package com.example.bspringboot.l_starter.myconditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author chenpiqian
 * @date: 2020-02-24
 */
public class MyOnPropertyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String[] properties = (String[])metadata.getAnnotationAttributes("com.example.bspringboot.l_starter.myconditional.MyConditionalOnProperty").get("value");
        for (String property:properties){
            if (!"true".equals(context.getEnvironment().getProperty(property))){
                return false;
            }
        }
        return true;
    }

}
