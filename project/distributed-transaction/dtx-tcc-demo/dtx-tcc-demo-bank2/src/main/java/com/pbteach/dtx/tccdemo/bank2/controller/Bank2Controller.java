package com.pbteach.dtx.tccdemo.bank2.controller;

import com.pbteach.dtx.tccdemo.bank2.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
@Slf4j
public class Bank2Controller {
    @Autowired
    AccountInfoService accountInfoService;

    @RequestMapping("/transfer")
    public Boolean transfer(@RequestParam("msg") String msg, @RequestParam("amount") Double amount) {
        // log.info("超时模拟");
        // try {
        //
        //     TimeUnit.SECONDS.sleep(10L);
        // }catch (Exception e){
        //     e.printStackTrace();
        // }

        this.accountInfoService.updateAccountBalance(msg, amount);
        return true;
    }

}
