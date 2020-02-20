注解方式配置bean的方式：
1、@Component声明
2、配置类中使用@Bean
3、实现FactoryBean
4、实现BeanDefinitionRegistryPostProcessor
5、继承ImportBeanDefinitionRegistrar


refreshContext方法堆栈
    refreshContext:397, SpringApplication (org.springframework.boot)
    run:315, SpringApplication (org.springframework.boot)
    
org.springframework.context.support.AbstractApplicationContext.refresh()作用：
    1、bean配置读取加载入口
    2、spring框架启动流程

org.springframework.context.support.AbstractApplicationContext.refresh()方法体内调用了13个子方法
    org.springframework.context.support.AbstractApplicationContext.prepareRefresh();
        设置容器状态：AbstractApplicationContext#this.active.set(true);
        初始化属性设置：AbstractApplicationContext.initPropertySources()
        检查必备属性是否存在：AbstractApplicationContext#getEnvironment().validateRequiredProperties();
    org.springframework.context.support.AbstractApplicationContext.obtainFreshBeanFactory()
        设置beanFactory的序列化id
        获取beanFactory
    org.springframework.context.support.AbstractApplicationContext.prepareBeanFactory()
        配置bean工厂属性
        添加后置处理器
        设置忽略的自动装配接口
        注册一些组件
    org.springframework.context.support.AbstractApplicationContext.postProcessBeanFactory()
        此方法没提供实现，由子类去实现。 本web工程的实现是AnnotationConfigServletWebServerApplicationContext.postProcessBeanFactory
        方法作用：子类重写postProcessBeanFactory()以便在BeanFactory完成创建后做进一步的设置
    org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors()
        调用BeanDefinitionRegistryPostProcessor实现容器内添加bean定义
        调用BeanFactoryPostProcessor实现向bean添加属性
    org.springframework.context.support.PostProcessorRegistrationDelegate.registerBeanPostProcessors()    
        找到BeanPostProcessor的实现
        排序后注册进容器内
    org.springframework.context.support.AbstractApplicationContext.initMessageSource()
        初始化国际化属性
    org.springframework.context.support.AbstractApplicationContext.initApplicationEventMulticaster()
        初始化事件广播器  
    org.springframework.context.support.AbstractApplicationContext.onRefresh()
        空实现，交给子类去实现。web容器中是ServletWebServerApplicationContext.onRefresh()            org.springframework.context.support.AbstractApplicationContext.registerListeners()
        想广播器注册监听器 
    org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization()
        初始化单例bean
    org.springframework.context.support.AbstractApplicationContext.finishRefresh()
        初始化声明周期处理器
        调用生命周期处理器onRefresh方法
        发布ContextRefreshedEvent事件  
        JMX相关处理
    org.springframework.context.support.AbstractApplicationContext.resetCommonCaches()
        清除缓存         

完成该上下文的bean工厂的初始化，初始化所有剩余的单例bean。
org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization()
    确保所有非延迟初始化单例都实例化
    org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons()
        org.springframework.beans.factory.support.RootBeanDefinition 此类用于描述bean
        添加端点条件 name.equals("getBeanTest")
        org.springframework.beans.factory.support.AbstractBeanFactory.getBean()
            使用默认构造器实例化bean，返回一个装饰器类BeanWrapper
            org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateBean()
        




