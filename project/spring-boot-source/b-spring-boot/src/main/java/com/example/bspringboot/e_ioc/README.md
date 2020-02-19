注解方式配置bean的方式：
1、@Component声明
2、配置类中使用@Bean
3、实现FactoryBean
4、实现BeanDefinitionRegistryPostProcessor
5、继承ImportBeanDefinitionRegistrar


refreshContext方法堆栈
    refreshContext:397, SpringApplication (org.springframework.boot)
    run:315, SpringApplication (org.springframework.boot)
    
org.springframework.context.support.AbstractApplicationContext.refresh()作用：
    1、bean配置读取加载入口
    2、spring框架启动流程

org.springframework.context.support.AbstractApplicationContext.refresh()方法体内调用了13个子方法



