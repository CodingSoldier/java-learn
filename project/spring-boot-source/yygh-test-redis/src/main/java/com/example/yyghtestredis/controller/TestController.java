package com.example.yyghtestredis.controller;


import com.example.yyghspringbootredisstarter.YyghRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
@Slf4j
public class TestController {

    @Autowired
    private static RedisTemplate redisTemplate;

    private static UserBean m = new UserBean();

    public void test01(){
        // 存储在栈空间
        String aKey = "aaaaaaa";

        // new ArrayList<>() 应用类型存储在堆空间
        ArrayList<Long> roleIds = new ArrayList<>();
        UserBean userBean = new UserBean(1L, "小明", roleIds);
        UserBean userBeanbbb = userBean;
        /*
         方法执行完后roleIds、userBean指向的对象无法再被使用，所以roleIds、userBean指向的对象的对象就变成了垃圾，需要回收
         */


    }

    @GetMapping("/test")
    public String test(){
        // new YyghRedisUtil<>(redisTemplate);

        String aKey = "aaa";
        YyghRedisUtil.set(aKey, "vvvv", 111L, TimeUnit.DAYS);
        // YyghRedisUtil.set("aaa", "vvvv");
        String a = YyghRedisUtil.get(aKey, String.class);
        log.info("a的值：{}", a);

        String userKey = "user01";
        ArrayList<Long> roleIds = new ArrayList<>();
        roleIds.add(100L);
        roleIds.add(200L);
        UserBean userBean = new UserBean(1L, "小明", roleIds);
        YyghRedisUtil.set(userKey, userBean);
        UserBean user01 = YyghRedisUtil.get(userKey, UserBean.class);
        log.info("user01的值：{}", user01);

        Boolean hasKey = YyghRedisUtil.hasKey("user01");
        log.info("是否存在key user01: {}", hasKey);

        String listKey = "list-key";
        ListOperations<String, String> listOps = YyghRedisUtil.getRedisTemplate().opsForList();
        LinkedList<String> values = new LinkedList<>();
        values.add("val1");
        values.add("val2");
        values.add("val3");
        values.add("val3");
        YyghRedisUtil.getRedisTemplate().delete(listKey);
        listOps.rightPushAll(listKey, values);
        List<String> range = listOps.range(listKey, 0, -1);
        log.info("range是: {}", range);

        String setKey = "set-key";
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        String[] valueArray = new String[values.size()];
        for (int i = 0; i < values.size(); i++) {
            valueArray[i]=values.get(i);
        }
        redisTemplate.delete(setKey);
        setOps.add(setKey, valueArray);
        Set<String> setVals = setOps.members(setKey);
        log.info("setVals是: {}", setVals);

        return "ok";
    }

}
