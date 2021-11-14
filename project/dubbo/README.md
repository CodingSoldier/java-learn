# RPC
RPC（Remote Procedure Call）是指远程过程调用，是一种进程间通信的方式，他是一种技术的思想，而不是规范，他允许程序调用另一个地址空间（通常是共享网络的一台机器上）的过程或函数，而不是程序员显式编码这个远程调用的细节。即程序员无论调用本地的还是远程的函数，本质上编写的调用代码基本相同。

# zookeeper使用
1、下载zookeeper-3.4.11，解压
2、修改 conf/zoo_sample.cfg 为 conf/zoo.cfg
3、打开 zoo.cfg
    修改 dataDir=../data
    查看关键信息 clientPort=2181
4、启动zk服务端。在 bin 目录下使用shell客户端运行 .\zkServer.cmd
5、启动zk客户端。在 bin 目录下使用shell客户端运行 .\zkCli.cmd

Zookeeper 是 Apache Hadoop 的子项目，是一个树型的目录服务，支持变更推送，适合作为 Dubbo 服务的注册中心，工业强度较高，可用于生产环境，并推荐使用
![](图片/zookeeper.jpg)

在zkCli运行
获取根节点的值
get /

查看当前节点
ls /

创建临时节点 /atguigu 值为 123456
create -e /atguigu 123456

获取节点信息
get /atguigu

# 安装管理控制台dubbo-admin
dubbo-admin，图形化的服务管理页面，安装时需要指定注册中心地址，即可从注册中心获取所有提供者/消费者进行配置管理。
github下载incubator-dubbo-ops-master
1、打开incubator-dubbo-ops-master\dubbo-admin\src\main\resources\application.properties
    查看注册中心IP、端口是否正确
    dubbo.registry.address=zookeeper://127.0.0.1:2181
2、进入incubator-dubbo-ops-master\dubbo-admin
    执行 mvn clean package -DskipTests
3、拷贝 dubbo-admin-0.0.1-SNAPSHOT.jar 到合适的地方，运行
    java -jar dubbo-admin-0.0.1-SNAPSHOT.jar
4、http://localhost:7001/
    账号密码默认是 root/root

# 开始使用dubbo
分包
建议将服务接口、服务模型、服务异常等均放在 API 包中，因为服务模型和异常也是 API 的一部分，这样做也符合分包原则：重用发布等价原则(REP)，共同重用原则(CRP)。
1、创建 gmall-interface 存放服务接口、Java Bean
2、创建服务提供者 user-service-provider
3、创建消费者 order-service-consumer


user-service-provider、order-service-consumer工程初始化
1、两个项目的pom.xml导入依赖
<!-- 导入dubbo依赖 -->
		<dependency>
			<groupId>com.alibaba</groupId>
			<artifactId>dubbo</artifactId>
			<version>2.6.2</version>
		</dependency>
<!-- 注册中心使用的是zookeeper，引入操作zookeeper的客户端端
dubbo2.6 之前使用zkclient操作zookeeper
dubbo2.6 之后使用curator操作zookeeper -->
		<dependency>
			<groupId>org.apache.curator</groupId>
			<artifactId>curator-framework</artifactId>
			<version>2.12.0</version>
		</dependency>
2、user-service-provider创建 resources/provider.xml
   user-service-consumer创建 resources/consumer.xml
   可参考dubbo的“快速开始”：https://dubbo.apache.org/zh/docs/v2.7/user/quick-start/
3、spring的依赖要用到5版本
	启动provider
4、进入	dubbo-admin 控制台，查看服务、应用，provider已经注册到dubbo
	http://localhost:7001/governance/services
	http://localhost:7001/governance/applications
5、运行provider、consumer

# 简单的监控中心dubbo-monitor-simple
1、进入incubator-dubbo-ops-master\dubbo-monitor-simple，打包
    执行 mvn clean package -DskipTests
3、拷贝 dubbo-monitor-simple-2.0.0-assembly.tar.gz 到合适的地方，解压
4、查看dubbo-monitor-simple-2.0.0\conf\dubbo.properties配置是否正确，例如注册中心的地址
运行assembly.bin\start.bat
4、打开 http://localhost:8080/


# 与spring-boot继承
https://github.com/apache/dubbo-spring-boot-project/blob/master/README_CN.md
步骤
1、新建boot-user-service-provider
2、pom.xml导入依赖
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>com.atguigu.gmall</groupId>
        <artifactId>gmall-interface</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba.boot</groupId>
        <artifactId>dubbo-spring-boot-starter</artifactId>
        <version>0.2.0</version>
    </dependency>
3、配置文件
	# 把xml中的配置使用配置文件的写法
	# dubbo应用名称
	dubbo.application.name=boot-user-service-provider3
	# 注册中心地址
	dubbo.registry.address=127.0.0.1:2181
	# 注册中心协议
	dubbo.registry.protocol=zookeeper

	# 指定通信规则（通信协议 & 通信端口）
	dubbo.protocol.name=dubbo
	dubbo.protocol.port=20880

4、UserServiceImpl接口使用注解，暴露服务 
	@Service //暴露服务 
	@Component
	注意@service注解是com.alibaba.dubbo.config.annotation.Service，不是spring的注解
5、启动类添加
	@EnableDubbo //开启基于注解的dubbo功能


1、新建boot-user-service-consumer
2、pom.xml导入依赖
	<dependency>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-web</artifactId>
	</dependency>
    <dependency>
        <groupId>com.atguigu.gmall</groupId>
        <artifactId>gmall-interface</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>com.alibaba.boot</groupId>
        <artifactId>dubbo-spring-boot-starter</artifactId>
        <version>0.2.0</version>
    </dependency>
3、配置文件
    # dubbo应用名称
    dubbo.application.name=boot-order-service-consumer
    # 注册中心地址
    dubbo.registry.address=zookeeper://127.0.0.1:2181
    
    # 监控中心，这么写是：通过注册中心找监控中心
    dubbo.monitor.protocol=registry

4、OrderServiceImpl注入远程调用类UserService
    /**
    * @Reference表示dubbo的远程调用类
    */
    @Reference(loadbalance = "roundrobin")//dubbo直连
    UserService userService;

5、启动类添加
	@EnableDubbo //开启基于注解的dubbo功能

6、最好先启动provider，然后再启动consumer

补充：
暴露服务，在service类上使用 @com.alibaba.dubbo.config.annotation.Service 注解
远程调用，使用 @Reference 注解


