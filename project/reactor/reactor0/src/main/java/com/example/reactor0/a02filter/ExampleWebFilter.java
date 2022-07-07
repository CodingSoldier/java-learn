package com.example.reactor0.a02filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器
 * @author chenpq05
 * @since 2022/7/7 10:13
 */
@Slf4j
@Component
public class ExampleWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getResponse().getHeaders().add("web-filter", "web-filter-test");
        log.info("####################进入全局过滤器ExampleWebFilter");
        return chain.filter(exchange);
    }
}
