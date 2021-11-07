package com.example.imagetest.hpa;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@RequestMapping("/hpa")
public class HpaController {

    public Map map = new HashMap();

    @RequestMapping("/mem")
    public String mem(@RequestParam("value") Integer value) {
        for (int i = 0; i < value; i++) {
            MemObj memObj = new MemObj(UUID.randomUUID().toString(), new Random().nextInt(Integer.MAX_VALUE));
            map.put(UUID.randomUUID().toString(), memObj);
        }
        long currMemory=Runtime.getRuntime().freeMemory();
        // System.out.println("目前可用的内存为："+currMemory/1024 + " k");
        return "ok";
    }

}
