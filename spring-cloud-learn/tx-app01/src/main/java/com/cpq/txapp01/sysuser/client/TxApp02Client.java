package com.cpq.txapp01.sysuser.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "tx-app002")
public interface TxApp02Client {
    @RequestMapping(value = "/sys/role/add",method = RequestMethod.POST)
    JSONObject add();
}
