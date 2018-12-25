1、tx-manager   
    1.1、配置application.properties中的eureka、redis
    1.2、启动eureka、启动tx-manager
2、启动微服务。
    2.1、启动springcloud-mybatis-demo1、springcloud-mybatis-demo2、api-gateway  
    2.2、发送请求测试
         http://localhost:1001/springcloud/mybatis/demo1/demo/save
         post
         {}
3、结果springcloud-mybatis-demo2的save方法确实执行了，但是T_TEST2中未插入数据，分布式事物回滚了。
             
    
# tx-manager

LCN 分布式事务协调器

创建zip安装包

`mvn clean install `