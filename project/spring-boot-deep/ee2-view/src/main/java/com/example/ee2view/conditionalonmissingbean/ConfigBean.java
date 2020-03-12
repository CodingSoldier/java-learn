package com.example.ee2view.conditionalonmissingbean;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenpiqian
 * @date: 2020-03-10
 */
@Configuration
public class ConfigBean {

    @Bean
    public BeanA beanA() {
        return new BeanA("beanA");
    }

    /**
     * 不会创建defaultBeanA，
     *
     * @Autowired BeanA defaultBeanA; 不报错，但注入的是beanA
     * <p>
     * https://juejin.im/post/5d4239785188255d5f403e05
     */
    @Bean
    @ConditionalOnMissingBean
    public BeanA defaultBeanA() {
        return new BeanA("defaultBeanA");
    }

    /**
     * 指定名称，IOC中无名称为defaultBeanA的时候才会创建beanA2，能创建beanA2
     */
    @Bean
    @ConditionalOnMissingBean(name = "defaultBeanA")
    public BeanA beanA2() {
        return new BeanA("beanA2");
    }

}
