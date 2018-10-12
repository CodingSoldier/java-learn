package com.demo.old.transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/transactional")
public class TransactionalCtrl {

    @Autowired
    TS01 service1;

    @GetMapping("/select")
    public String select() throws Exception {
        service1.select01();
        return "结束";
    }

    @GetMapping("/update01")
    public String update01() throws Exception {
        service1.update01();
        return "结束";
    }

}
