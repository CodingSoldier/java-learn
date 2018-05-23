package com.cpq.validatetest.service;

import com.cpq.validatetest.validate.ValidateInterface;
import com.cpq.validatetest.validate.bean.Config;
import com.cpq.validatetest.validate.bean.ResultValidate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateInterfaceImpl implements ValidateInterface, InitializingBean {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String basePath() {
        return "validate/";
    }

    @Override
    public Object validateNotPass(ResultValidate resultValidate) {
        Map<String, Object> r = new HashMap<>();
        r.put("success", resultValidate.isPass());
        r.put("msg", resultValidate.getMsgSet());
        return r;
    }

    @Override
    public Map<String, Object> getCache(Config config) {
        //String key = createKey(config);
        //return redisTemplate.opsForHash().entries(key);
        return new HashMap<>();
    }

    @Override
    public void setCache(Config config, Map<String, Object> json) {
        //String key = createKey(config);
        //redisTemplate.opsForHash().putAll(key, json);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //ExecutorService es = Executors.newCachedThreadPool();
        //es.submit(new Runnable() {
        //    @Override
        //    public void run() {
        //        Set<String> keys = redisTemplate.keys(basePath().replace("/",":") + "*");
        //        redisTemplate.delete(keys);
        //    }
        //});
    }

    ////创建缓存key
    //private String createKey(Config config){
    //    String basePath = Utils.trimBeginEndChar(basePath(), '/') + "/";
    //    String fileName = config.getFile().substring(0, config.getFile().lastIndexOf(".json"));
    //    fileName = Utils.trimBeginEndChar(fileName, '/');
    //    String jsonKey = config.getKeyName();
    //    jsonKey = Utils.isBlank(jsonKey) ? jsonKey : (":"+jsonKey);
    //    String temp = basePath + fileName + jsonKey;
    //    return temp.replaceAll("[\\/\\-]",":");
    //}

}
