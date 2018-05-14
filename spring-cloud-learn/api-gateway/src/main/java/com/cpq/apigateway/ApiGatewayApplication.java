package com.cpq.apigateway;

import com.cpq.apigateway.filter.AccessFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	/**
	 * 通过 http://localhost:1101/eureka-client-01/dc?token=false来测试过滤器
	 */
	@Bean
	public AccessFilter accessFilter(){
		return new AccessFilter();
	}
}
