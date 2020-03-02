package com.example.dflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class RouterConfig {

    @Autowired
    private DemoHandler demoHandler;

    /**
     * webflux函数式写法
     */
    @Bean
    public RouterFunction<ServerResponse> demoRouter(){
        return RouterFunctions
                .route(RequestPredicates.GET("/hello"), demoHandler::hello)
                .andRoute(RequestPredicates.GET("/world"), demoHandler::world)
                .andRoute(RequestPredicates.GET("/times"), demoHandler::times);
    }

}
