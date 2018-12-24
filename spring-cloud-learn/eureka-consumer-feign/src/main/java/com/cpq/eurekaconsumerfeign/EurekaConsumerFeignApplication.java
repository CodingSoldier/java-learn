package com.cpq.eurekaconsumerfeign;

import com.cpq.eurekaconsumerfeign.service.MyMessageConverter;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaConsumerFeignApplication {
    @Bean
    public Decoder feignDecoder(){
        MyMessageConverter wxConverter = new MyMessageConverter();
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(wxConverter);
        return new SpringDecoder(objectFactory);
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerFeignApplication.class, args);
    }

}
