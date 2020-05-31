package com.example.cpq.hconfiguration02;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@EnableAutoConfiguration
public class C_Environment_BeanFactoryAware_EnvironmentAware implements BeanFactoryAware, EnvironmentAware {

    Environment environment;

    /**
     * BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory) 先执行
     * EnvironmentAware#setEnvironment(org.springframework.core.env.Environment)    后执行
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.environment = beanFactory.getBean(Environment.class);

        // hashcode是相等的
        System.out.println(this.environment.hashCode());
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        System.out.println(this.environment.hashCode());
    }

    @Bean
    public User02 user02(){
        Long id = environment.getRequiredProperty("user.id", Long.class);
        String name = environment.getRequiredProperty("user.name", String.class);
        Integer ageNew = environment.getProperty("user.ageNew", Integer.class, 100);

        User02 user02 = new User02();
        user02.setId(id);
        user02.setName(name);
        user02.setAge(ageNew);
        return user02;
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(C_Environment_BeanFactoryAware_EnvironmentAware.class)
                .web(WebApplicationType.NONE)
                .run(args);

        User02 user02 = app.getBean(User02.class);
        System.err.println("用户bean"+user02);

        app.close();
    }


}
