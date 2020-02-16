spring-boot启动流程：
    框架初始化
    框架启动
    自动化装配

框架初始化步骤：
    配置资源加载器、配置primarySources、应用环境监测（是否web环境、是否reactive）、系统初始化器、配置应用监听器、配置main方法所在类

启动框架：
    计时器开始计时、headless模式赋值（没有I/O设备）、发送ApplicationStartingEvent、配置环境模块（environment对象）、发送ApplicationEnvironmentPreparedEvent、打印banner、创建应用上下文对象、初始化失败分析器、关联springboot组件与应用上下文对象、发送ApplicationContextInitializedEvent、加载sources到context、发送ApplicationPreparedEvent、刷新上下文（完成bean加载）、计时器停止计时、发送ApplicationStartedEvent、调用框架启动扩展类、发送ApplicationReadyEvent

框架自动化装配步骤：
    收集配置文件的配置工厂类、加载组件工厂、注册组件内定义bean

框架初始化 的 系统初始化器介绍：
    类名：ApplicationContextInitializer
    介绍: spring容器刷新之前执行的一个回调函数
    作用：向springboot容器注册属性
    使用：继承接口自定义实现








