package com.cpq.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-03
 */
@RestController
@RequestMapping("/cluster")
public class ClusterCtrl {

    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/get")
    public Object test001(){
        Object value = "";
        try {
            String key = "two:" + new Date().getTime();
            redisTemplate.opsForValue().set(key, new Date().getTime());
            TimeUnit.MILLISECONDS.sleep(100L);
            System.out.println(redisTemplate.opsForValue().get(key));
            value = redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            e.printStackTrace();
            value = "异常";
        }
        return value;
    }

}
