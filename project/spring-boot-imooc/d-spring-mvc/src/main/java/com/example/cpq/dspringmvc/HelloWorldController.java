package com.example.cpq.dspringmvc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * HelloWorld {@link Controller}
 *
 * @author 小马哥
 * @since 2018/5/20
 */
@Controller
public class HelloWorldController {

    @RequestMapping("/")
    public String index() {
        System.out.println(new ResponseEntity<Map>(HttpStatus.OK));
//        model.addAttribute("acceptLanguage",acceptLanguage);
//        model.addAttribute("jsessionId",jsessionId);
//        model.addAttribute("message","Hello,World");
        return "index";
    }

}
