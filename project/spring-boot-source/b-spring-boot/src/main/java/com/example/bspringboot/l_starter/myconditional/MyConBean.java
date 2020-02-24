package com.example.bspringboot.l_starter.myconditional;

import org.springframework.stereotype.Component;

@Component
@MyConditionalOnProperty({"bean.my.conditional1", "bean.my.conditional2"})
/**
 * 执行流程：
 @MyConditionalOnProperty 使用了 @Conditional(MyOnPropertyCondition.class)
 MyOnPropertyCondition.java实现了Condition接口
 MyOnPropertyCondition#matches()获取注解@MyConditionalOnProperty的value。
 获取value在property中的值，若未在property中配置为true，返回false；否则返回true
 */
public class MyConBean {
}
