package com.cpq.paramsvalidateboot.service;


import com.cpq.paramsvalidateboot.validate.Utils;
import com.cpq.paramsvalidateboot.validate.ValidateInterface;
import com.cpq.paramsvalidateboot.validate.bean.Config;
import com.cpq.paramsvalidateboot.validate.bean.ResultValidate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ValidateImpl implements ValidateInterface, InitializingBean {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String basePath() {
        return "config/validate/json/";
    }

    @Override
    public Object validateNotPass(ResultValidate resultValidate) {
        return resultValidate;
    }

    @Override
    public Map<String, Object> getCache(Config config) {
        String key = createKey(config);
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public void setCache(Config config, Map<String, Object> json) {
        String key = createKey(config);
        redisTemplate.opsForHash().putAll(key, json);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ExecutorService es = Executors.newCachedThreadPool();
        es.submit(new Runnable() {
            @Override
            public void run() {
                Set<String> keys = redisTemplate.keys(basePath().replace("/",":") + "*");
                redisTemplate.delete(keys);
            }
        });
    }

    //创建缓存key
    private String createKey(Config config){
        String basePath = Utils.trimBeginEndChar(basePath(), '/') + "/";
        String fileName = config.getFile().substring(0, config.getFile().lastIndexOf(".json"));
        fileName = Utils.trimBeginEndChar(fileName, '/');
        String jsonKey = config.getKeyName();
        jsonKey = Utils.isBlank(jsonKey) ? jsonKey : (":"+jsonKey);
        String temp = basePath + fileName + jsonKey;
        return temp.replaceAll("[\\/\\-]",":");
    }

}
