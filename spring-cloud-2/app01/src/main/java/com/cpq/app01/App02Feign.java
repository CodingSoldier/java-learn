package com.cpq.app01;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-13
 */
@FeignClient(value = "app02")
@RequestMapping("/test02")
public interface App02Feign {
    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Object test02Post(@RequestBody Map<String, String> map);
}
