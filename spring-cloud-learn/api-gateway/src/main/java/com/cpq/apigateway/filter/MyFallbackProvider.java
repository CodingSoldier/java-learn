package com.cpq.apigateway.filter;

// https://www.cnblogs.com/niechen/p/8856551.html  ZuulFallbackProvider


import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

//  https://www.cnblogs.com/niechen/p/8856551.html  ZuulFallbackProvider

//   http://itmuch.com/spring-cloud/edgware-new-zuul-fallback/

//周立  http://itmuch.com/categories/Spring-Cloud/

@Component
public class MyFallbackProvider implements FallbackProvider {
    Logger logger = LoggerFactory.getLogger(FallbackProvider.class);
    @Override
    public String getRoute() {
        // 表明是为哪个微服务提供回退，*表示为所有微服务提供回退
        return "*";
    }
    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        logger.error("服务异常", cause);
        if (cause instanceof HystrixTimeoutException) {
            return response(HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return fallbackResponse();
        }
    }
    @Override
    public ClientHttpResponse fallbackResponse() {
        return response(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ClientHttpResponse response(final HttpStatus status) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return status;
            }
            @Override
            public int getRawStatusCode() throws IOException {
                return status.value();
            }
            @Override
            public String getStatusText() throws IOException {
                return status.getReasonPhrase();
            }
            @Override
            public void close() {
            }
            @Override
            public InputStream getBody() throws IOException {
                Map<String, Object> map = new HashMap<>();
                map.put("code", 500);
                map.put("msg", "服务不可用，请稍后再试。");
                return new ByteArrayInputStream(JSON.toJSONString(map).getBytes());
            }
            @Override
            public HttpHeaders getHeaders() {
                // headers设定
                HttpHeaders headers = new HttpHeaders();
                MediaType mt = new MediaType("application", "json", Charset.forName("UTF-8"));
                headers.setContentType(mt);
                return headers;
            }
        };
    }
}