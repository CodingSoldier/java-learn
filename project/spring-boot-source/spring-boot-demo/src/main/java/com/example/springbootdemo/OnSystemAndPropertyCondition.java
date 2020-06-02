package com.example.springbootdemo;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class OnSystemAndPropertyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnSystemAndProperty.class.getName());
        String propertyName = String.valueOf(attributes.get("name"));
        boolean propertyValue = Boolean.parseBoolean(String.valueOf(attributes.get("value")));

        String javaPropertyValue = System.getProperty(propertyName);



        return propertyValue.equals(javaPropertyValue);
    }

}