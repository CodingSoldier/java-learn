package com.demo.websocket;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/websocket")
public class SocketController {

    // utils.post('/websocket/pushVideoListToWeb',{sdf:1111})
    @PostMapping("/pushVideoListToWeb")
    @ResponseBody
    public Map<String,Object> pushVideoListToWeb(@RequestBody Map<String,Object> param) {
        Map<String,Object> result =new HashMap<String,Object>();

        try {
            WebSocketServer.sendInfo("有新客户呼入,sltAccountId:" + param.toString());
            result.put("operationResult", true);
        }catch (IOException e) {
            result.put("operationResult", true);
        }
        return result;
    }



}