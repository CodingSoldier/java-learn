1、tx-manager   
    1.1、配置application.properties中的eureka、redis
    1.2、启动tx-manager、启动eureka、启动api-gateway
2、启动微服务。
    2.1、配置springcloud-mybatis-demo1、springcloud-mybatis-demo2
        1、pom.xml基础配置  
        2、application.properties必须配置以下内容
            tm.manager.url=http://127.0.0.1:7000/tx/manager/
            spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
            hystrix、ribbon
        3、配置TxManagerTxUrlServiceImpl.java、TxManagerHttpRequestServiceImpl.java
    2.2 事务开始方添加注解
           @TxTransaction(isStart = true)
           @Transactional 
        事务参与方添加注解
           @TxTransaction
           @Transactional             
    2.3、发送请求测试
         http://localhost:1001/springcloud/mybatis/demo1/test01/save
         post
         {}
3、结果springcloud-mybatis-demo2的save方法确实执行了，但是T_TEST2中未插入数据，分布式事物回滚了。
             
    
# tx-manager

LCN 分布式事务协调器

创建zip安装包

`mvn clean install `