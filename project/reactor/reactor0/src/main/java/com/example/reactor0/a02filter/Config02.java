package com.example.reactor0.a02filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Slf4j
@Configuration
public class Config02 {

    @Bean
    public RouterFunction<ServerResponse> route(PlayerHandler playerHandler) {
        return RouterFunctions.route(GET("/players/{name}"), playerHandler::getName)
                .filter(new ExampleHandlerFilterFunction());
    }

    /**
     * 需添加到 RouterFunctions.route
     */
    public class ExampleHandlerFilterFunction implements HandlerFilterFunction<ServerResponse, ServerResponse> {
        @Override
        public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
            log.info("@@@@@@@@@@@@进入ExampleHandlerFilterFunction");
            return next.handle(request);
        }
    }

}
