package com.example.ee3rest.controller3;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

/**
 * @author chenpiqian
 * @date: 2020-03-13
 */
@RestController
public class ControllerConverter {

    @PostMapping(value = "/add/props",
    consumes = "text/properties",
    produces = "text/properties")
    public Properties addProps(@RequestBody Properties prop) {
        return prop;
    }


    @PostMapping(value = "/test/resolver",
    consumes = "text/properties",
    produces = "text/properties")
    public Properties testResolver( Properties prop) {
        return prop;
    }

}
