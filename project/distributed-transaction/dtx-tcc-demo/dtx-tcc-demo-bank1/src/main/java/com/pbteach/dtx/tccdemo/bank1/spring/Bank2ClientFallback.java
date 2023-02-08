package com.pbteach.dtx.tccdemo.bank1.spring;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Administrator
 * @version 1.0
 **/
@Component
public class Bank2ClientFallback implements Bank2Client {

    @Override
    public Boolean transfer(String msg, Double amount) {

        return false;
    }
}
