package com.example.demo.test01.client;

import com.example.demo.test01.entity.Test;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by lorne on 2017/6/27.
 */
@FeignClient(value = "springcloud-mybatis-demo2",fallback = Demo2ClientHystric.class)
public interface Demo2Client {


    @RequestMapping(value = "/test02/list",method = RequestMethod.GET)
    List<Test> list();


    @RequestMapping(value = "/test02/save",method = RequestMethod.GET)
    int save();
}
