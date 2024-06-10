package com.example.mqttclientpaho.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/mqtt")
public class WebHookController {
    private static final Logger log  = LoggerFactory.getLogger(WebHookController.class);

    private Map<String,Boolean> clientStatus = new HashMap<>();
    
    @PostMapping("/webhook")
    public void hook(@RequestBody Map<String,Object> params){
        log.info("emqx 触发 webhook,请求体数据={}",params);

        String action = (String) params.get("action");
        String clientId = (String) params.get("clientid");
        if(action.equals("client_connected")){
            log.info("客户端{}接入本系统",clientId);
            clientStatus.put(clientId,true);
        }
        
        if(action.equals("client_disconnected")){
            log.info("客户端{}下线",clientId);
            clientStatus.put(clientId,false);
        }
        
    }
    
    @GetMapping("/allStatus")
    public Map getStatus(){
        return this.clientStatus;
    }
}
