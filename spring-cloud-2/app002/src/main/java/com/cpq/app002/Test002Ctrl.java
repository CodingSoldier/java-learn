package com.cpq.app002;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-03
 */
@RestController
@RequestMapping("/test02")
public class Test002Ctrl {

    @GetMapping("/get/{id}")
    public String test02Get(@PathVariable("id") Integer id){
        return "test02/get/"+id;
    }

    @PostMapping("/post")
    public Map<String, String> test02Post(@RequestBody Map<String, String> map){
        map.put("app", "app002");
        return map;
    }

    @PostMapping("/timeout1")
    public Map<String, String> timeout1(@RequestBody Map<String, String> map) throws Exception{
        TimeUnit.SECONDS.sleep(600L);
        return map;
    }

}
