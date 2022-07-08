package com.example.reactor0.a03error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * WebFlux也可以使用@RestControllerAdvice
 * @RestControllerAdvice会导致 GlobalErrorWebExceptionHandler 失效
 */
@Slf4j
@RestControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(Exception.class)
    protected Mono<ResponseEntity> errorHandler(Exception e) {
        log.error("全局异常MyControllerAdvice", e);
        Mono<ResponseEntity> r = Mono.just(ResponseEntity.badRequest().body("全局异常MyControllerAdvice：" + e.getMessage()));
        // return ResponseEntity.badRequest().body("全局异常MyControllerAdvice："+e.getMessage());
        return r;
    }

}
