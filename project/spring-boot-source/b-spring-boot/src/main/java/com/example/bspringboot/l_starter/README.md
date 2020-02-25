starter

@Conditional
    含义：基于条件的注解
    作用：根据是否满足某一个条件来决定是否创建特定bean
    意义：SpringBoot实现自动配置的关键

@ConditionalOnBean           存在bean时生效
@ConditionalOnMissingBean    不存在bean时生效
@ConditionalOnClass            class存在则生效
@ConditionalOnMissingClass     class不存在则生效
@ConditionalOnWebApplication   web环境生效
@ConditionalOnProperty         包含特定属性时生效
@ConditionalOnNotWebApplication  非web环境生效
@ConditionalOnJava                特定java版本生效     


starter介绍：
    简介：可插拔插件
    与jar包区别：starter能实现自动配置
    作用：大幅提升开发效率


1、自定义一个starter：c-spring-boot-weather-starter
2、引入依赖
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>c-spring-boot-weather-starter</artifactId>
        <version>0.0.1</version>
    </dependency>
3、配置：
    weather.enable=true
    weather.type=wt
    weather.rate=wr
4、使用WeatherStarterTest测试

starter原理
org.springframework.boot.autoconfigure.AutoConfigurationImportSelector#getAutoConfigurationEntry
    使用SpringFactoriesLoader加载org.springframework.boot.autoconfigure.EnableAutoConfiguration的实现类
    org.springframework.boot.autoconfigure.AutoConfigurationImportSelector.getCandidateConfigurations
        org.springframework.boot.autoconfigure.condition.OnPropertyCondition.Spec.collectProperties 添加断点条件key.equals("weather.enable")

starter自动配置类导入
    启动类上加@SpringBootApplication
    引入AutoConfigurationImportSelector
    在ConfigurationClassParser中处理
    获取spring.factories中EnableAutoConfiguration实现


