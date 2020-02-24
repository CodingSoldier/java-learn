package com.example.bspringboot.l_starter.conditional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
/**
 当配置了bean.aaa.conditional属性时，将此类加入到ioc容器
 @ConditionalOnProperty 源码配置了 @Conditional(OnPropertyCondition.class)
 */
@ConditionalOnProperty("bean.aaa.conditional")
public class ConBean {
}
