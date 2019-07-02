package com.cpq.app01;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-13
 */

//@FeignClient与@RequestMapping不能同时存在接口上，spring官方似乎不愿修复这坑爹问题
@FeignClient(name = "app02", fallback =App02FeignHystrix.class )
public interface App02Feign {

    @GetMapping(value = "/test02/get/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String test02Get(@PathVariable("id") Integer id);

    @PostMapping(value = "/test02/post", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Map<String, String> test02Post(@RequestBody Map<String, String> map);

}
