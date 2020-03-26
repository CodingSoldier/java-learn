package com.example.multidatasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonCtrl {

    @Autowired
    CommonService commonService;

    @GetMapping("/test01")
    public boolean test01(@RequestParam("msg") String msg){
        boolean b = commonService.save(msg);
        return b;
    }

}
