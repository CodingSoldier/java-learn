package com.cpq.apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-03-27
 */
@Component
public class AuthFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("进入AuthFilter");
        return chain.filter(exchange);

        //boolean isAllow = true;
        //if (isAllow) {
        //    return chain.filter(exchange);
        //
        //} else {
        //    //设置status和body
        //    return Mono.defer(() -> {
        //        //setResponseStatus(exchange, HttpStatus.UNAUTHORIZED);
        //        final ServerHttpResponse response = exchange.getResponse();
        //        byte[] bytes = "Hello World".getBytes(StandardCharsets.UTF_8);
        //        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        //        response.getHeaders().set("aaa", "bbb");
        //        return response.writeWith(Flux.just(buffer));
        //    });
        //}

    }
}