package com.example.s2apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class S2ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(S2ApiGatewayApplication.class, args);
    }



    //@Bean
    //public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient) {
    //    return new DiscoveryClientRouteDefinitionLocator(discoveryClient);
    //}

}
