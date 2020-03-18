SpringFactoriesLoader介绍：
    框架内部使用的通用工厂加载机制
    从classpath下的多个jar包特定位置读取文件并初始化类
    文件内容必须是kv形式，即properties类型
    key是全限定名（抽象类、接口），value是实现;多个value用逗号分隔
    
SpringFactoriesLoader加载spring.factories文件，
读取到org.springframework.context.ApplicationContextInitializer=com.example.bspringboot.c_initializer.FirstInitializer时就初始化自定义的系统初始化器
    

系统初始化器被调用的流程：  
com.example.bspringboot.BSpringBootApplication.main()执行构造函数SpringApplication()
org.springframework.boot.SpringApplication#SpringApplication()执行下面代码    
setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
org.springframework.boot.SpringApplication.getSpringFactoriesInstances(java.lang.Class<T>)分析：
org.springframework.boot.SpringApplication.getSpringFactoriesInstances(java.lang.Class<T>, java.lang.Class<?>[], java.lang.Object...)执行下面代码：
    Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));解析
        type是org.springframework.context.ApplicationContextInitializer
        org.springframework.core.io.support.SpringFactoriesLoader.loadSpringFactories()解析：
            org.springframework.core.io.support.SpringFactoriesLoader.loadSpringFactories()
            读取META-INF/spring.factories配置，除了读取本项目中配置的spring.factories，还会读所有jar中的spring.factories，例如：spring-beans-5.2.3.RELEASE.jar!/META-INF/spring.factories
            并将spring.factories中的key-value配置转换为Properties对象
            将properties转为result（LinkedMultiValueMap类型），最后将result设置到缓存中，并返回result
            result最终有多个key-value，value是LinkedList，比如org.springframework.context.ApplicationContextInitializer对应的value除了本项目配置的还有jar包自带的 
    Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader))解析：
        names是spring.factories文件中org.springframework.context.ApplicationContextInitializer对应的value
    List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names)使用names（类全路径）创建类实例集合
    AnnotationAwareOrderComparator.sort(instances)对类集合排序，排序规则是按照@Order(数字)从小到大排序
源代码    
setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
就将org.springframework.context.ApplicationContextInitializer配置value设置给了到了org.springframework.boot.SpringApplication.initializers属性


org.springframework.context.ApplicationContextInitializer实现类的作用：
    上下文刷新即refresh方法前调用
    用来编码设置一些属性变量，通常用在web环境中
    可以通过order接口进行排序
    
  
  
  
  
    

