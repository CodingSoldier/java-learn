package com.pbteach.dtx.tccdemo.bank1.controller;

import com.pbteach.dtx.tccdemo.bank1.service.Bank1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @version 1.0
 **/
@RestController
public class Bank1Controller {
    @Autowired
    Bank1Service accountInfoService;

    @RequestMapping("/transfer")
    public Boolean transfer(@RequestParam("msg") String msg, @RequestParam("amount") Double amount) {
        this.accountInfoService.updateAccountBalance(msg, amount);
        return true;
    }

}
