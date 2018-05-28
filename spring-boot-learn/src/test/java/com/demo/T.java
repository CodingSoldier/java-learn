package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.demo.config.AppProp;
import com.demo.paramsvalidate.Utils;
import com.demo.testgenerator.mapper.TestGeneratorMapper;
import com.demo.testgenerator.model.TestGenerator;
import com.demo.testgenerator.model.TestGeneratorExample;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RunWith(SpringRunner.class)
@SpringBootTest
public class T {

    @Autowired
    AppProp appProp;
    @Autowired
    TestGeneratorMapper testGeneratorMapper;

    @Test
    public void test11(){
        TestGeneratorExample example = new TestGeneratorExample();

        List<TestGenerator> l = testGeneratorMapper.selectByExample(example);
        System.out.println(l);
    }

    @Test
    public void test() {
        System.out.println(appProp.getA());
    }

    @Test
    public void jackson() throws IOException{
        String path = "config/validate/json-post.json";
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(path);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map = mapper.readValue(is, Map.class);
        System.out.println(map);
    }
    @Test
    public void gson() throws Exception{
        String path = "config/validate/test-gson.json";
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(path);
        Map<String, Object> map = new HashMap<>();
        Object gson = Gson.class.newInstance();
        Method method = Gson.class.getMethod("fromJson", Reader.class, Class.class);
        map = (Map<String, Object>)method.invoke(gson, new BufferedReader(new InputStreamReader(is)), Map.class );
        Gson gson1 = new Gson();
        map = gson1.fromJson(new InputStreamReader(is), Map.class);

        System.out.println(map);
        System.out.println(Gson.class.getName());

    }
    @Test
    public void fastjson() throws Exception{
        Class[] arr = {JSON.class, Feature[].class};
        String path = "config/validate/test-fast-json.json";
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(path);
        Map<String, Object> map = new HashMap<>();
        Method method = arr[0].getMethod("parseObject", InputStream.class, Type.class, arr[1]);
        map = (Map<String, Object>)method.invoke(null, is, Map.class, null);

        //map = JSON.parseObject(is, Map.class);
        System.out.println(map);
    }

    public void ttt1(String... str){
        System.out.println("sdfahkfjhakfjha");
    }

    @Test
    public void test2() {
        ttt1();

    }

    @Test
    public void test3() {
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

