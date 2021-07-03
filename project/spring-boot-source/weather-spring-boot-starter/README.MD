创建starter的流程
1、pom.xml加入spring-boot-starter依赖
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <scope>compile</scope>
    </dependency>

2、新建XxxProperties承载application.yaml/application.properties的属性

3、新建XxxxAutoConfiguration类
    3.1、类上加上注解@EnableConfigurationProperties(XxxProperties.class)，启用属性类
         类上加上注解@ConditionalOnProperty(name = "xxx.enable", havingValue = "true")
    3.2、在XxxxAutoConfiguration使用@Bean创建IOC bean

4、新建/resources/META-INF/spring.factories。添加自动化装配类全路径
org.springframework.boot.autoconfigure.EnableAutoConfiguration=com.xxx.xxx.XxxAutoConfiguration


