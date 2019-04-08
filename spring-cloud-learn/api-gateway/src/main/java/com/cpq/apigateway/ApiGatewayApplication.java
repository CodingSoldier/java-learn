package com.cpq.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}


	@Bean
	@LoadBalanced
	public RestTemplate restTemplateBalanced(){
		return new RestTemplate();
	}
	/**
	 * 通过 http://localhost:1101/eureka-client-01/dc?token=false来测试过滤器
	 */
	//@Bean
	//public AccessFilter accessFilter(){
	//	return new AccessFilter();
	//}
}
