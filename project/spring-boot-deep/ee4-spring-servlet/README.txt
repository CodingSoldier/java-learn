初始化servlet策略
    DispatcherServlet.initStrategies(ApplicationContext context)
    系统启动的时候不会运行，第一次调用接口的时候才会执行。


启动工程
idea新建configuration ——> Remote
拷贝  -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
运行jar包
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 ee4-spring-servlet-0.0.1-SNAPSHOT-war-exec.jar

localhost:8080/hello-world
    DeferredResultMethodReturnValueHandler.handleReturnValue()
    由于使用了@ResponseBody注解，返回值由HandlerMethodReturnValueHandler的子类DeferredResultMethodReturnValueHandler来处理





