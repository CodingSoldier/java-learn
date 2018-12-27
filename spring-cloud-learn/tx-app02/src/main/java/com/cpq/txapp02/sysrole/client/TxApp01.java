package com.cpq.txapp02.sysrole.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "tx-app01")
public interface TxApp01 {

    @RequestMapping(value = "/sys/user/add",method = RequestMethod.POST)
    JSONObject save();

}
