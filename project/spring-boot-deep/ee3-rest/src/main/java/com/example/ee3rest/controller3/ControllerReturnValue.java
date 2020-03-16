package com.example.ee3rest.controller3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Properties;


@Controller
public class ControllerReturnValue {

    @PostMapping(value = "/test/return/value",
    consumes = "text/properties",
    produces = "text/properties")
    public Properties returnValue( Properties prop) {
        return prop;
    }

}
