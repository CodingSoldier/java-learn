package com.example.reactor0.a06urlpattern;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @GetMapping("/spring5/{*id}")
    public String URIVariableHandler(@PathVariable String id) {
        return id;
    }

}
