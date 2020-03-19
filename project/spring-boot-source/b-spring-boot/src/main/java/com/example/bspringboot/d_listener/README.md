ApplicationListener 应用程序事件监听器

@FunctionalInterface 注解的类只能有一个抽象方法

ApplicationEventMulticaster 应用程序广播器，管理监听器并广播Event

事件发送顺序：
    框架启动、starting、environmentPrepared、contextInitialized、prepared、started、ready、启动完毕
    

       
    


