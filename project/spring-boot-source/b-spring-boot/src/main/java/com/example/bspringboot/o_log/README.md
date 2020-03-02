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









