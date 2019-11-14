package com.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 该注解指定项目为springboot，由此类当作程序入口
 * 自动装配 web 依赖的环境
 **/
@SpringBootApplication
public class SpringbootApplication {

    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(SpringbootApplication.class, args);
    }

    //@Bean
    //public RestTemplate restTemplate(){
    //    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    //    httpRequestFactory.setConnectionRequestTimeout(2000);
    //    httpRequestFactory.setConnectTimeout(5000);
    //    httpRequestFactory.setReadTimeout(60000);
    //
    //    RestTemplate restTemplate = new RestTemplate();
    //    restTemplate.setRequestFactory(httpRequestFactory);
    //    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    //    return restTemplate;
    //}

}