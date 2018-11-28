package com.cpq.apigateway.filter;

import com.alibaba.fastjson.JSON;
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
import java.util.HashMap;
import java.util.Map;

// https://www.cnblogs.com/niechen/p/8856551.html  ZuulFallbackProvider

//  https://www.cnblogs.com/niechen/p/8856551.html  ZuulFallbackProvider

//   http://itmuch.com/spring-cloud/edgware-new-zuul-fallback/

//周立  http://itmuch.com/categories/Spring-Cloud/

@Component
public class GatewayFallback implements FallbackProvider {

    private Logger logger = LoggerFactory.getLogger(GatewayFallback.class);

    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return createClientHttpResponse(null);
    }

    @Override
    public ClientHttpResponse fallbackResponse(Throwable throwable) {
        return createClientHttpResponse(throwable);
    }

    private ClientHttpResponse createClientHttpResponse(Throwable throwable){
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.OK.value();
            }

            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.OK.toString();
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                logger.error("网关抛异常", throwable);
                Map<String, Object> result = new HashMap<>();
                result.put("code", 100000);
                result.put("msg", "系统异常,请稍后重试");
                result.put("exception", throwable == null ? null:throwable);
                return new ByteArrayInputStream(JSON.toJSONString(result).getBytes());
            }

        };
    }
}