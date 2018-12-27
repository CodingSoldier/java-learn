package com.cpq.txapp01.sysuser.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by lorne on 2017/6/27.
 */
@FeignClient(value = "springcloud-mybatis-demo1")
public interface Demo1Client {
    @RequestMapping(value = "/test01/save2",method = RequestMethod.GET)
    int save();
}
