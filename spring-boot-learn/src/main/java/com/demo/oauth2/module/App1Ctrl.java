package com.demo.oauth2.module;


import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app1")
public class App1Ctrl {

    @PostMapping("/post/map")
    public Object p1(@RequestBody Map<String, Object> map) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", map);
        return map1;
    }

    @GetMapping("/get/a1")
    public Object a1(String r1) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", r1);
        return map1;
    }

}
