package com.cpq.consulclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-03-27
 */
@RestController
public class CheckController {

    @GetMapping("check")
    public String check(){
        System.out.println("**************ok1111");

        return "ok1";
    }

    @GetMapping("test01")
    public String test01(){
        System.out.println("**************test01");
        return "test01";
    }

    @GetMapping("/api/consul/client/test001")
    public String test011(){
        System.out.println("**************/api/consul/client/test001");
        return "/api/consul/client/test001";
    }

    @GetMapping("/consulclient/test001")
    public String test01122(){
        System.out.println("*************consulclient/test001");
        return "consulclient/test001";
    }
}
