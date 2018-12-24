package com.cpq.txapp01.sysuser.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;


@Component
public class TxApp02ClientHystric implements TxApp02Client {

    @Override
    public JSONObject add() {
        System.out.println("进入断路器-save。。。");
        throw new RuntimeException("save 保存失败.");
    }
}
