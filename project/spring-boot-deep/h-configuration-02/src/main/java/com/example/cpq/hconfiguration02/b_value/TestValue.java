package com.example.cpq.hconfiguration02.b_value;

import com.example.cpq.hconfiguration02.User02;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
public class TestValue {

    /**
     * 在方法参数上使用 @Value()
     * @Value("${user.age.no:100}  没有值，则会取100
     * @Value("${user.age.no:${user.city.post-code:100}}")
     * 没配置user.age.no，取user.city.post-code；还是没有，取100
     */
    //@Bean
    //public User02 user02(@Value("${user.id}") Long id, @Value("${user.name}")String name, @Value("${user.age.no:100}")Integer age){
    //    return new User02(id, name, age);
    //}
    @Bean
    public User02 user02(@Value("${user.id}") Long id, @Value("${user.name}")String name,
                         @Value("${user.age.no:${user.city.post-code:100}}")Integer age){
        return new User02(id, name, age);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext app = new SpringApplicationBuilder(TestValue.class)
                .web(WebApplicationType.NONE)
                .run(args);

        User02 user02 = app.getBean("user02", User02.class);
        System.out.println(user02);

        app.close();
    }

}
