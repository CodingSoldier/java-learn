package com.example.reactor0.a03error;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author chenpq05
 * @since 2022/7/7 14:32
 */
@Configuration
public class Config03 {

    @Bean
    public RouterFunction<ServerResponse> routeRequest(Handler handler) {
        return RouterFunctions.route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), handler::handleWithErrorReturn);
    }

}
