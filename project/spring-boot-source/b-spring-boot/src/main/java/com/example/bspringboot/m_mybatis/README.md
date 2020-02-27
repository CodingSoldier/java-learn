mybatis-starter导入了spring-boot-starter，所有starter都需要导入starter

mybatis-spring-boot-autoconfigure-2.1.1.jar!/META-INF/spring.factories 配置
    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration,\
    org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
我们主要用的是org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration，这个类是SQL写到xml中的配置类

org.mybatis.spring.boot.autoconfigure.MybatisProperties 获取以mybatis开头的属性

org.springframework.boot.autoconfigure.jdbc.DataSourceProperties 获取以spring开头的数据源属性

缓存过期时间=固定时间 + 一个随机时间值  可以避免缓存雪崩

缓存穿透
    原因：恶意攻击去查询数据库不一定不存在的数据，对数据库造成压力
    解决：使用特殊缓存空值标识对象不存在
缓存雪崩
    原因：在某一个时间段，缓存集中过期失效，对数据造成周期性压力波峰
    解决：过期时间加上随机时间
    













