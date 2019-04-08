package com.cpq.eurekaconsumerfeign.controller;

import com.cpq.eurekaconsumerfeign.service.DcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DcController {

    @Autowired
    DcClient dcClient;

    @GetMapping("/consumer")
    public String dc(){
        return dcClient.consumer();
    }

    @PostMapping("/p1")
    public Map p1(@RequestBody Map map){
        System.out.println(map.toString());
        return map;
    }

}
