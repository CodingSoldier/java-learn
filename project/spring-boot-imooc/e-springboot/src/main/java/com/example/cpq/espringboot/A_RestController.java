package com.example.cpq.espringboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class A_RestController {

    @GetMapping("/test01")
    public String test01(){
        return "test01";
    }

}
