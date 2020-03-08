package com.example.cpq.dspringmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * HelloWorld {@link Controller}
 *
 * @author 小马哥
 * @since 2018/5/20
 */
@Controller
public class HelloWorldController {

    @RequestMapping("")
    public String index() {
        /**
         * 返回值是view name
         * ViewResolver通过view name生成View
         */
        return "index";
    }

    //    @RequestMapping("")
//    public String index(@RequestParam int value, Model model) {
//        System.out.println(new ResponseEntity<Map>(HttpStatus.OK));
////        model.addAttribute("acceptLanguage",acceptLanguage);
////        model.addAttribute("jsessionId",jsessionId);
////        model.addAttribute("message","Hello,World");
//
//        /**
//         * 返回值是view name
//         * ViewResolver通过view name生成View
//         */
//        return "index";
//    }

}
