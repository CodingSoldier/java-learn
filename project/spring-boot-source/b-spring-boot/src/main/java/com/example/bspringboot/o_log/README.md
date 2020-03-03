spring-boot目前使用slf4j+logback的组合
    slf4j日志接口
    logback日志实现

org.slf4j.LoggerFactory.getILoggerFactory()
    int INITIALIZATION_STATE 使用volatile修饰，
    判断INITIALIZATION_STATE是否等于某值，等于则修改，需要用到双重检查
        if (INITIALIZATION_STATE == UNINITIALIZED) {
            synchronized (LoggerFactory.class) {
                if (INITIALIZATION_STATE == UNINITIALIZED) {
                    INITIALIZATION_STATE = ONGOING_INITIALIZATION;
                    performInitialization();
                }
            }
        }
    其他代码修改INITIALIZATION_STATE，判断条件却不是INITIALIZATION_STATE本身，则直接赋值即可，没用双重检查


加入依赖
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
        <version>2.12.1</version>
    </dependency>
日志寻址    
org.slf4j.LoggerFactory.findPossibleStaticLoggerBinderPathSet 返回集合的size是2

logger.debug("只在debug日志级别打印日志，但是直接使用字符串拼接的形式，仍然会执行字符串拼接，但不打印日志。消耗性能" + obj)
logger.debug("使用占位符大括号{}作为占位符，就只会在debug模式下才执行字符串替换{}", obj1, obj2)

logback日志配置信息：
    <configuration scan="true" scanPeriod="60 seconds" debug="false"/>
    scan：设置为true时，配置文件若发生变化，将会重新加载
    scanPeriod：扫描时间间隔，若没给出时间单位，默认为毫秒
    debug：若设置为true，将打印出logback内部日志信息















