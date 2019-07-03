package com.cpq.app001;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-03
 */

@RestController
@RequestMapping("/test01")
public class Test01Ctrl {

    @Autowired
    App002Feign app02Feign;


    @GetMapping("/get")
    public String test01Get(){
        System.out.println("/test01/get");
        return "test01/get";
    }

    @GetMapping("/get/timeout")
    public String gettimeout() throws Exception{
        System.out.println("/test01/get");
        TimeUnit.MINUTES.sleep(60L);
        return "test01/get";
    }

    @PostMapping("/post")
    public Object test01Post(@RequestBody Map<String, String> map){
        System.out.println(map.toString());
        map.put("app", "app001");
        return map;
    }



    @PostMapping("/test02/post")
    public Object test0102Post(@RequestBody Map<String, String> map){
        System.out.println(map.toString());
        map.put("feign", "feign");
        return app02Feign.test02Post(map);
    }

    @GetMapping("/test02/get/{id}")
    public String test01Get(@PathVariable("id") Integer id){
        return app02Feign.test02GetId(id);
    }

    @PostMapping("/test02/timeout1")
    public Map<String, String> timeout1(@RequestBody Map<String, String> map){
        return app02Feign.timeout1(map);
    }

}
