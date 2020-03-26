配置类解析器

org.springframework.context.annotation.ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry()
代码逻辑：
    获得BeanDefinitionRegistry的唯一id: registryId
    检查一下registryId是否已处理过
    添加registryId到已处理集合中
    processConfigBeanDefinitions   
    
org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions()
打断点，设置断点条件 beanName.equals("BSpringBootApplication")

 
org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions()
    do{
    }while()循环

org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass
    判断Bean注解


org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass()
    org.springframework.context.annotation.ConfigurationClassParser.processMemberClasses

IOC中的bean使用BeanDefinition描述。BeanDefinition描述一个bean实例，该实例具有属性值，构造函数参数值以及具体实现所提供的更多信息。BeanFactoryPostProcessor用于修改属性值和其他bean元数据
