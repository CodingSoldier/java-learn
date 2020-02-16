package com.example.bspringboot.controller;

import com.example.bspringboot.util.ApplicationUtil;
import com.example.bspringboot.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class MyController {

    @Autowired
    private DemoService demoService;


    @RequestMapping("/hello/{id}")
    @ResponseBody
    public String hello(@PathVariable(value = "id") Long id) {
        return Optional
                .ofNullable(demoService.getDemoById(id))
                .orElse("empty String");
    }

    @RequestMapping("/elem/{id}")
    @ResponseBody
    public String elem(@PathVariable(value = "id") String id) {
        return ApplicationUtil.getApplicationContext()
                .getEnvironment().getProperty("key"+id);
    }


}
