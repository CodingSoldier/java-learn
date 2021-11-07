package com.example.shirojwt.controller;

import com.example.shirojwt.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestOtherCtrl {

    @GetMapping("/other")
    public Result other() {
        return Result.success("other");
    }


}
