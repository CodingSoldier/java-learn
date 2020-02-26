mybatis-starter导入了spring-boot-starter，所有starter都需要导入starter

mybatis-spring-boot-autoconfigure-2.1.1.jar!/META-INF/spring.factories 配置
    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration,\
    org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
我们主要用的是org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration，这个类是SQL写到xml中的配置类

org.mybatis.spring.boot.autoconfigure.MybatisProperties 获取以mybatis开头的属性

org.springframework.boot.autoconfigure.jdbc.DataSourceProperties 获取以spring开头的数据源属性