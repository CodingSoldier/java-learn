package com.example.cpq.espringboot;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
public class P_Controller {

    /**
     * RestWebMvcConfigurer#extendMessageConverters(java.util.List) 加入PropertiesHttpMessageConverter实例
     * 前端指定 Content-Type = text/properties；
     * body指定参数：
     *    aaa:aksdfjal
     *    bb:154546
     * 请求参数经过 PropertiesHttpMessageConverter#readInternal() 反流序列化成 Properties
     * 请求到达本方法
     * 响应结果Properties经过PropertiesHttpMessageConverter#writeInternal() 序列化成流
     */
    @PostMapping(value = "/props",
        consumes = "text/properties;charset=UTF-8") // Content-Type 过滤媒体类型
    public Properties props(@RequestBody Properties properties){
        properties.put("response", "value01");
        return properties;
    }

}
