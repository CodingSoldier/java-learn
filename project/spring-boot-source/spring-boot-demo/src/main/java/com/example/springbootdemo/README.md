https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config

17种属性配置方式，优先级由高到低，前面存在键值会忽略后面的配置，可以理解为短路现象：
	1、Devtools全局设置属性在你的主目录(~/.spring-boot-devtools.properties当devtools处于激活状态时）。
	2、测试中的@TestPropertySource注解
        @SpringBootTest
        @TestPropertySource({"classpath:application-test-property-source.properties"})                 
	3、测试中的@SpringBootTestproperties注解属性
        @SpringBootTest(properties = {"test.property=3、测试中的@SpringBootTestproperties注解属性"})
	4、命令行参数
	    Program Arguments 添加 --test.property=4、命令行参数
	5、来自SPRING_APPLICATION_JSON（嵌入在环境变量或系统属性中的内联JSON）的属性
	    idea 环境变量中添加
	    Environment variables 中添加 
         	name=SPRING_APPLICATION_JSON
         	value={"test.property":"5、来自SPRING_APPLICATION_JSON（嵌入在环境变量或系统属性中的内联JSON）的属性"}
        
        +
    6、ServletConfig初始化参数
    7、ServletContext初始化参数
    8、java:comp/env中的JNDI属性
    9、Java系统属性（System.getProperties()）
            System.setProperty("test.property", "9、Java系统属性（System.getProperties()）");
            
    10、操作系统环境变量
        java -jar spring-boot-demo-0.0.1-SNAPSHOT.jar --spring.config.additional-location=C:\Users\Administrator\Desktop\my-location\ --spring.profiles.active=dev -Dtest.property="10、操作系统环境变量"
        
    11、一个只有random.*属性的RandomValuePropertySource
            配置 property.random=${random.int[20,30]}
            
    12、在你的jar包之外的 特殊配置文件的 应用程序属性（application-{profile}.properties和YAML 变体）
        在my-location目录中加入application-dev.properties文件
        java -jar spring-boot-demo-0.0.1-SNAPSHOT.jar --spring.config.additional-location=C:\Users\Administrator\Desktop\my-location\ --spring.profiles.active=dev
        
    13、打包在jar中的 特殊配置文件的 应用程序属性（application-{profile}.properties和YAML 变体）
        java -jar spring-boot-demo-0.0.1-SNAPSHOT.jar --spring.config.additional-location=C:\Users\Administrator\Desktop\my-location\ --spring.profiles.active=dev
        
    14、在你的jar包之外的应用程序属性（application.properties和YAML 变体）
        新建my-location目录加入application.properties文件
        使用命令 java -jar spring-boot-demo-0.0.1-SNAPSHOT.jar --spring.config.additional-location=C:\Users\Administrator\Desktop\my-location\
        使用spring.config.additional-location配置时，除了默认位置外，还使用它们，在默认位置之前搜索额外的位置。
        
    15、打包在jar中的应用程序属性（application.properties和YAML 变体）
    16、@PropertySource注解在你的@Configuration类上，对yaml文件无效
            配置properties-16.properties
            BSpringBootApplication.java加上@PropertySource({"properties-test.properties"})
            最好使用@PropertySource({"classpath:properties-16.properties"})不然跟第2、3点会冲突
    17、默认属性（通过设置SpringApplication.setDefaultProperties指定）
            SpringApplication springApplication = new SpringApplication(BSpringBootApplication.class);
            Properties properties = new Properties();
            properties.setProperty("property.key", "使用第17种默认属性配置方式");
            springApplication.setDefaultProperties(properties);
            springApplication.run(args);
    
Aware介绍
    Spring框架优点：Bean感知不到容器的存在
    Aware使用场景：需要使用spring容器的功能资源
    使用Aware的缺点：Bean和容器强耦合  
    
org.springframework.beans.factory.Aware 
    Ctrl+H 查看子层级类
    
aware_processor包模仿MyEnvironmentAware#setEnvironment()源码的
    org.springframework.context.support.ApplicationContextAwareProcessor.postProcessBeforeInitialization()
        org.springframework.context.support.ApplicationContextAwareProcessor#invokeAwareInterfaces()

org.springframework.boot.SpringApplication.prepareEnvironment
准备环境
    org.springframework.boot.SpringApplicationRunListeners.environmentPrepared
    listeners.environmentPrepared(environment);
    监听       
        org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.load()
            initializeProfiles()
                this.profiles.add(null);  
                首先添加了一个null元素，可以把 application.properties 的profile认为是null

                Set<Profile> activatedViaProperty = getProfilesFromProperty(ACTIVE_PROFILES_PROPERTY);
                Set<Profile> includedViaProperty = getProfilesFromProperty(INCLUDE_PROFILES_PROPERTY);                
                此时还没加载配置文件，只能先加载命令行配置的spring.profiles.active、spring.profiles.include

                if this.profiles.size() == 1 
                this.profiles.add(defaultProfile);
                如果命令行没有配置 spring.profiles.active、spring.profiles.include 就加一个default的profile
    
            initializeProfiles()执行完后
            this.profiles=[null, 命令行中配置的spring.profiles.active和spring.profiles.include/或者default]


org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#load()
    ConfigFileApplicationListener.Loader.getSearchLocations()
    查找配置文件的路径，默认会查找4个路径 classpath:/,classpath:/config/,file:./,file:./config/
    可以通过 --spring.config.additional-location 添加新的配置文件路径

    org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.getSearchNames()
    若没配置spring.config.name，则获取文件名，不带文件类型 application
    这样是为了能加载properties和yaml文件

    org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#load()
    使用this.propertySourceLoaders加载配置文件
    this.propertySourceLoaders有2个loader：PropertiesPropertySourceLoader（"properties", "xml"）、YamlPropertySourceLoader（"yml", "yaml"）

        org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#loadForFileExtension
        加载配置文件

可以看出来：
    1、先循环路径
    2、循环加载器，不同的加载器加载不同的配置文件类型
    3、通过加载器在路径中加载配置文件    

org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#loadDocuments
    List<PropertySource<?>> loaded = loader.load(name, resource);
    将文件加载到this.loadDocumentsCache
        org.springframework.boot.env.PropertiesPropertySourceLoader#load
        Properties文件加载器
            org.springframework.boot.env.OriginTrackedPropertiesLoader#load(boolean)
            加载方法        
    先将文件加载为PropertySource对象
    documents = asDocuments(loaded);
    将loaded转为document对象
    this.loadDocumentsCache.put(cacheKey, documents);
    将document添加到缓存

org.springframework.core.env.MutablePropertySources#addLast
    this.propertySourceList.add(propertySource);
    将配置文件生成的对象添加到 this.propertySourceList 中，this.propertySourceList是一个 new CopyOnWriteArrayList<>()

// org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#loadForFileExtension()
//     刚开始时 profile == null，加载application.properties
//     然后读取到application.properties中配置spring.profiles.active、spring.profiles.include，判断条件profile != null，读取spring.profiles.active、spring.profiles.include配置文件
// org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.asDocuments()处理application.properties中的spring.profiles.active、spring.profiles.include
    List<Document> documents = loadDocuments(loader, name, resource);    
    获取到的documents包含两个属性activeProfiles、includeProfiles
    documents的propertySource的source就包含了配置文件中的键值对test.property
    
org.springframework.core.env.MutablePropertySources.addLast
    MutablePropertySources类 将source添加到 List<PropertySource<?>> propertySourceList = this.propertySourceList  
    this.propertySourceList.add(propertySource);  

    org.springframework.boot.context.config.ConfigFileApplicationListener.Loader 
    的 loaded 属性定义是 Map<Profile, MutablePropertySources> loaded
    
    即ConfigFileApplicationListener.Loader -> loaded属性是一个map，key是profile名称，value是MutablePropertySources对象
    MutablePropertySources的this.propertySourceList属性的PropertySource对象的source就存储了配置文件的key-value
      

load(...)然后加载默认的配置文件比如application.properties
    加载application.properties后会读取application.properties中配置的spring.profiles.active、spring.profiles.include
        再加载spring.profiles.active、spring.profiles.include中配置的文件

    
回到org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.load()
    this.loaded是LinedHashMap{key是profile: value是配置文件}。this.loaded..source是配置文件中的键值
    this.loaded中默认配置文件application.properties的key是null  
    
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#addLoadedPropertySource()
    MutablePropertySources destination = this.environment.getPropertySources(); 获取环境配置集合
    this.environment 的 destination 是各种配置类型例如：
        OriginTrackedMapPropertySource是由properties文件创建的对象
        JsonPropertySource包含了SPRING_APPLICATION_JSON的配置
  
  
org.springframework.core.env.PropertySourcesPropertyResolver.getProperty()
循环出PropertyResolver中的每个配置类    
    org.springframework.boot.context.properties.source.ConfigurationPropertySourcesPropertySource.findConfigurationProperty()
    循环Environment的PropertySources

    value = resolveNestedPlaceholders((String) value);
    处理占位符，占位符没有先后顺序

