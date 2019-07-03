package com.cpq.app001;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-06-13
 */
@Component
public class App002FeignHystrix implements App002Feign {

    @Override
    public String test02GetId(Integer id) {
        return "降级test02Get"+id;
    }

    @Override
    public Map<String, String> test02Post(Map<String, String> map){
        map.put("熔断-降级", "test02Post");
        return map;
    }

    @Override
    public Map<String, String> timeout1(Map<String, String> map) {
        map.put("熔断-降级", "timeout1");
        return map;
    }
}
