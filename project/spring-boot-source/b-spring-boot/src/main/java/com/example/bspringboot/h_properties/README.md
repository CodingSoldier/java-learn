17种属性配置方式，优先级由高到低，前面存在键值会忽略后面的配置，可以理解为短路现象：
    1、Devtools全局配置
    2、测试环境@TestPropertySource注解
        @SpringBootTest
        @TestPropertySource({"classpath:properties-test2.properties"})                 
    3、测试环境properties
        @SpringBootTest(properties = {"property.key=3、测试环境properties"})
    4、命令行参数
        添加程序参数 --property.key=第4种
    5、SPRING_APPLICATION_JSON属性
        添加程序参数 --SPRING_APPLICATION_JSON={\"property.key\":\"第5种\"}
    6、ServletConfig初始化参数
    7、ServletContext初始化参数
    8、JNDI属性
    9、JAVA系统属性
            property.vm.name=${java.vm.name}
    10、操作系统环境配置
    11、RandomValuePropertySource随机值属性
            配置 property.random=${random.int[20,30]}
    12、jar包外的application-{profile}.properties
    13、jar包内的application-{profile}.properties
    14、jar包外application.properties
    15、jar包内application.properties
    16、@PropertySource绑定外部文件配置
            配置properties-16.properties
            BSpringBootApplication.java加上@PropertySource({"properties-test.properties"})
            最好使用@PropertySource({"classpath:properties-16.properties"})不然跟第2、3点会冲突
    17、默认属性
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
            
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.load()
    initializeProfiles()先加载命令行配置的profile
    load(...)然后加载默认的配置文件比如application.properties
        加载application.properties后会读取application.properties中配置的spring.profiles.active、spring.profiles.include
            再加载spring.profiles.active、spring.profiles.include中配置的文件
    但有意思的是，即便获取了命令行中的配置，但是this.profiles=[null, 命令行中配置的profile]，在循环处理this.profiles的时候，由于第一个元素是null，仍然是先处理默认配置文件application.properties
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#initializeProfiles()
    获取程序启动参数中配置的--spring.profiles.active、--spring.profiles.include配置的profile，此方法不会处理配置文件中的这两个属性，只会处理程序启动参数中的这两个配置。为什么只有命令行中配置的参数才生效？因为此时任然未加载任何properties配置文件，this.profiles仍然是一个[null]。后面的代码会查找命令行--spring.profiles.default的配置，没有配置则返回default  
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.getSearchLocations()
    判断是否配置了spring.config.location，若没有则查找默认路径，共配置了4个classpath:/,classpath:/config/,file:./,file:./config/
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.getSearchNames()
    若没配置spring.config.name，则获取默认的配置文件前缀application 
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.load(String location, String name, Profile profile, DocumentFilterFactory filterFactory, DocumentConsumer consumer)
    this.propertySourceLoaders有2个loader：PropertiesPropertySourceLoader（"properties", "xml"）、YamlPropertySourceLoader（"yml", "yaml"）开始加载默认配置文件，比如application.properties
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#loadForFileExtension()
    刚开始时 profile == null，加载application.properties
    然后读取到application.properties中配置spring.profiles.active、spring.profiles.include，判断条件profile != null，读取spring.profiles.active、spring.profiles.include配置文件
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.asDocuments()处理application.properties中的spring.profiles.active、spring.profiles.include
    List<Document> documents = loadDocuments(loader, name, resource);    
    获取到的documents包含两个属性activeProfiles、includeProfiles
回到org.springframework.boot.context.config.ConfigFileApplicationListener.Loader.load()
    this.loaded是LinedHashMap{key是profile: value是配置文件}。this.loaded..source是配置文件中的键值
    this.loaded中默认配置文件application.properties的key是null  
org.springframework.boot.context.config.ConfigFileApplicationListener.Loader#addLoadedPropertySource()
    MutablePropertySources destination = this.environment.getPropertySources(); 获取环境配置集合
    将this.load..source中的配置添加到destination当中，配置文件的key-value添加给environment就完成了
  
  
    
    