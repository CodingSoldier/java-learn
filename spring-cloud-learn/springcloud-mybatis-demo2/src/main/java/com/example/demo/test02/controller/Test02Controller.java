package com.example.demo.test02.controller;


import com.example.demo.test02.entity.Test;
import com.example.demo.test02.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lorne on 2017/6/26.
 */
@RestController
@RequestMapping("/test02")
public class Test02Controller {

    @Autowired
    private DemoService demoService;


    @RequestMapping("/list")
    @ResponseBody
    public List<Test> list(){
        return demoService.list();
    }


    @RequestMapping("/save")
    @ResponseBody
    public int save(){
        int num = demoService.save();
        System.out.println("执行----com.example.demo.test02.controller.Test02Controller.save");
        return num;
    }
}
