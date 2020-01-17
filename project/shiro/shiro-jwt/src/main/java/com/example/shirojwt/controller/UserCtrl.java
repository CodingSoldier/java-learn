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
@RequestMapping("/user")
public class UserCtrl {

    @GetMapping("/list")
    public Result list(){
        return Result.success("/user/list");
    }

    @GetMapping("/view")
    public Result view(){
        return Result.success("/user/view");
    }

    @GetMapping("/add")
    public Result add(){
        return Result.success("/user/add");
    }

    @GetMapping("/edit")
    public Result edit(){
        return Result.success("/user/edit");
    }

    @GetMapping("/delete")
    public Result delete(){
        return Result.success("/user/delete");
    }

}
