package com.cpq.eurekaconsumerfeign.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class TxApp02ClientHystric implements TxApp02Client {

    @Override
    public JSONObject add() {
        System.out.println("进入断路器-save。。。");
        throw new RuntimeException("save 保存失败.");
    }
}
