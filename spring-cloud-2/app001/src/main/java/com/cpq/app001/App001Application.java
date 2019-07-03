package com.cpq.app001;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients
public class App001Application {

    public static void main(String[] args) {
        SpringApplication.run(App001Application.class, args);
    }

}
