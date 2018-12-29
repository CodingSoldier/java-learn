package com.example.demo.test01.controller;


import com.example.demo.test01.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lorne on 2017/6/26.
 */
@RestController
@RequestMapping("/test01")
public class Test01Controller {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/save")
    @ResponseBody
    public int save(){
        return demoService.save();
    }

    @RequestMapping("/save2")
    @ResponseBody
    public int save2(){
        return demoService.save2();
    }

    @RequestMapping("/restSave")
    @ResponseBody
    public int restSave(){
        return demoService.restSave();
    }


}
