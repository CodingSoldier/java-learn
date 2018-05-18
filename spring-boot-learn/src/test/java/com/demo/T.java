package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class T {



    @Test
    public void test() {
        System.out.println(JSON.parseObject(new StringBuffer().toString()));
    }

    @Test
    public void test1() {
        //JSONObject jsonObject = Util.readFileToJSONObject("config/validate/v1/postMap.json");
        //System.out.println(jsonObject);

    }

    @Test
    public void test2() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map22 = new HashMap<String, Object>();
        map1.put("a1", "a11111");
        map22.put("a222", "a2222");
        map1.put("a2", map22);
        map1.put("l1", Arrays.asList(1, 2, "1sd"));

        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map1));

        System.out.println(jsonObject);
    }
}

