package com.zs.diveinspringboot.externalizated.configuration.bootstrap;

import com.zs.diveinspringboot.externalizated.configuration.domain.User;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import static org.springframework.context.ConfigurableApplicationContext.ENVIRONMENT_BEAN_NAME;

@EnableAutoConfiguration
public class ValueAnnotationBootstrap implements BeanFactoryAware, EnvironmentAware {

    @Autowired
    @Qualifier(ENVIRONMENT_BEAN_NAME)
    private Environment environment;

//    private final Environment environment = null;

//    @Autowired
//    public ValueAnnotationBootstrap(Environment environment){
//        this.environment = environment;
//    }

//    private final Long id;
//
//    private final String name;
//
//    private Integer age;
//
//    public ValueAnnotationBootstrap(@Value("${user.id}") Long id, @Value("${user.name}") String name,
//                     @Value("${user.age}") Integer age) {
//        this.id = id;
//        this.name = name;
//        this.age = age;
//    }

    @Bean
    public User user2(
            //Environment environment
    ){
        Long id = environment.getRequiredProperty("user.id", Long.class);
        String name = environment.getRequiredProperty("user.name");
        Integer age = environment.getProperty("user.age", Integer.class,
                environment.getProperty("my.user.age",Integer.class, 31));
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        return user;
    }

    @Bean
    public User user(@Value("${user.id}") Long id, @Value("${user.name}") String name,
                     @Value("${user.age:${my.user.age:32}}") Integer age) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setAge(age);
        return user;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(ValueAnnotationBootstrap.class)
                .web(WebApplicationType.NONE)//非web应用
                .run(args);

        User user = applicationContext.getBean("user", User.class);
        User user2 = applicationContext.getBean("user2", User.class);

        System.out.println("用户对象: " + user);
        System.out.println("用户对象: " + user2);


        //关闭上下文
        applicationContext.close();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if(this.environment != beanFactory.getBean(ENVIRONMENT_BEAN_NAME,Environment.class)){
            throw new IllegalStateException();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
