package com.imooc.b_ioc_learn;

import com.imooc.b_ioc_learn.factory.PrefixUserFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author chenpiqian
 * @date: 2020-07-14
 */
@Configuration
@ComponentScan("com.imooc")
public class B_Bean {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        String xmlPath = "D:\\third-code\\java-learn\\project\\spring-source\\spring-demo1\\src\\main\\resources\\spring-config.xml";
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext(xmlPath);

        //得到无参构造函数创建的对象:
        User user1a = (User) applicationContext.getBean("user1");
        User user1b = (User) applicationContext.getBean("user1");
//得到静态工厂创建的对象：
        User user2a = (User) applicationContext.getBean("user2");
        User user2c = (User) applicationContext.getBean("user2");
//得到实例工厂创建的对象：
        User user3a = (User) applicationContext.getBean("user3");
        User user3b = (User) applicationContext.getBean("user3");

        System.out.println("无参构造函数创建的对象:" + user1a);
        System.out.println("无参构造函数创建的对象:" + user1b);
        System.out.println("静态工厂创建的对象：" + user2a);
        System.out.println("静态工厂创建的对象：" + user2c);
        System.out.println("实例工厂创建的对象：" + user3a);
        System.out.println("实例工厂创建的对象：" + user3b);

        /**
         org.springframework.beans.factory.BeanFactory#FACTORY_BEAN_PREFIX
            对FactoryBean的转义定义，提供获取FactoryBean的方式
            如果使用bean的名字检索FactoryBean得到的对象是工厂生成的对象
            如果需要用到工厂本身，需要转义
         */
        PrefixUserFactoryBean prefixUserFactoryBean = (PrefixUserFactoryBean)applicationContext.getBean("&prefixUserFactoryBean");
        System.out.println("获取FactoryBean实现类本身，需要加&前缀"+prefixUserFactoryBean);

        User user4 = (User)applicationContext.getBean("prefixUserFactoryBean");
        System.out.println("获取FactoryBean实现类getObject()的返回的bean"+user4);

    }

    /**
     jdk通过java.lang.Class描述对象
     spring通过BeanDefinition描述bean
        scope（@Scope）作用范围：
            singleton单例。
            prototype每次getBean()就创建一个，相当于new
            request
            session
            ......
        lazy-init（@Lazy）是否懒加载：
        首选primary（@Primary）优先注入的类
        factory-bean和factory-method（@Configuration和@Bean）
     */

    /**
     ApplicationContext常用容器
        FileSystemXmlApplicationContext  从文件系统加载配置
        ClassPathXmlApplcationContext    从classpath加载配置
        XmlWebApplicationContext         用于web应用程序的容器

        spring-boot的容器
        AnnotationConfigServletWebServerApplicationContext
        AnnotationConfigReactiveWebServerApplicationContext
        AnnotationConfigApplicationContext
     */
}
