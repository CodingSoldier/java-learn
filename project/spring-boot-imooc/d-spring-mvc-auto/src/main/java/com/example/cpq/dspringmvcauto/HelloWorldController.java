package com.example.cpq.dspringmvcauto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class HelloWorldController {

    @RequestMapping("/index")
    public String index() {
        System.out.println(new ResponseEntity<Map>(HttpStatus.OK));
        return "index";
    }

}
