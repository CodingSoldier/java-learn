package com.example.springbootdemo;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

public class OnSystemAndPropertyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 注解属性
        Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnSystemAndProperty.class.getName());
        String propertyName = String.valueOf(attributes.get("name"));
        String propertyValue = String.valueOf(attributes.get("value"));

        // 配置文件属性
        String property = context.getEnvironment().getProperty(propertyName, "");

        // 系统属性
        String systemUserName = System.getProperty("user.name");

        // ConditionalOnSystemProperty注解属性等于配置文件属性，并且系统用户名是Administrator，则注解条件成立
        return property.equals(propertyValue) && "Administrator".equals(systemUserName);
    }
}