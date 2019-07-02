package com.cpq.app01;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-13
 */
@Component
public class App02FeignHystrix implements App02Feign{

    @Override
    public String test02Get(Integer id) {
        return "降级test02Get"+id;
    }

    @Override
    public Map<String, String> test02Post(Map<String, String> map){
        map.put("熔断-降级", "熔断-降级");
        return map;
    }
}
