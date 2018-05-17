package com.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.validate.Util;
import com.demo.validate.bean.ResultCheck;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import javax.validation.constraints.Null;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class T {

    public static String trimBegin(String args, char beTrim) {
        if (Util.isEmpty(args) || Util.isEmpty(beTrim)){
            return "";
        }

        int st = 0;
        int len = args.length();
        char[] val = args.toCharArray();
        char sbeTrim = beTrim;
        while ((st < len) && (val[st] <= sbeTrim)) {
            st++;
        }
        //while ((st < len) && (val[len - 1] <= sbeTrim)) {
        //    len--;
        //}
        return ((st > 0) || (len < args.length())) ? args.substring(st, len) : args;
    }

    @Test
    public void test() {
        System.out.println(JSON.parseObject(new StringBuffer().toString()));
    }

    @Test
    public void test1() {
        //JSONObject jsonObject = Util.readFileToJSONObject("config/validate/v1/postMap.json");
        //System.out.println(jsonObject);

        System.out.println(Util.isEmpty("  ") || "".equals("  ".trim()));
    }

    @Test
    public void test2() {
        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map22 = new HashMap<String, Object>();
        map1.put("a1", "a11111");
        map22.put("a222", "a2222");
        map1.put("a2", map22);
        map1.put("l1", Arrays.asList(1,2,"1sd"));

        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(map1));

        System.out.println(jsonObject);
    }

    @Test
    public void testVo() {
        ResultCheckVo vo = new ResultCheckVo();
        vo.setA("aaaaaaaa");
        vo.setBb("bbbbbb");
        vo.setPass(false);
        HashSet<String> set = new HashSet<String>();
        set.add("sss11");
        set.add("ss2222");
        vo.setMsgSet(set);

        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString((Object) vo));
        System.out.println(jsonObject);
    }

    @Test
    public void testVo11() throws Exception{
        //JsonParser.Feature
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "{\"name\":\"Mahesh Kumar\", \"age\":21,\"verified\":false,\"marks\": [100,90,85]}";
        JsonNode rootNode = mapper.readTree(jsonString);
        System.out.println(rootNode);
    }

    @Test
    public void testVo22() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();

        ResultCheckVo vo = new ResultCheckVo();
        vo.setA("aaaaaaaa");
        vo.setBb("bbbbbb");
        vo.setPass(false);
        HashSet<String> set = new HashSet<String>();
        set.add("sss11");
        set.add("ss2222");
        vo.setMsgSet(set);
        User user = new User();
        user.setName("userName");
        vo.setUser(user);


        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = ResultCheckVo.class;
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }

        for (int i = 0, len = fieldList.size(); i < len; i++) {
            String varName = fieldList.get(i).getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fieldList.get(i).isAccessible();
                // 修改访问控制权限
                fieldList.get(i).setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fieldList.get(i).get(vo);
                if (o != null)
                    map.put(varName, o);


            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(map);
    }

    @Test
    public void testMap() throws Exception{
        Map<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> map1 = new HashMap<String, Object>();
        Map<String, Object> map22 = new HashMap<String, Object>();
        map1.put("a1", "a11111");
        map22.put("a222", "a2222");
        map1.put("a2", map22);
        map1.put("l1", Arrays.asList(1,2,"1sd"));

        System.out.println(null instanceof Null);

        List<Field> fieldList = new ArrayList<>() ;
        Class tempClass = Map.class;
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }

        for (int i = 0, len = fieldList.size(); i < len; i++) {
            String varName = fieldList.get(i).getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fieldList.get(i).isAccessible();
                // 修改访问控制权限
                fieldList.get(i).setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fieldList.get(i).get(map1);
                if (o != null)
                    map.put(varName, o);


            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(map);
    }

    @Test
    public void regex() throws Exception{
        String content = "/get/path/{id}/sav/{asdfjadf}";
        //Set<String> param
        Matcher m = Pattern.compile("(?<=\\{)[^\\}]+").matcher(content);
        while (m.find()) {
            System.out.println("Found value: " + m.group() );
        }
    }

    @Test
    public void isBean() throws Exception{
        int obj = 1;
        System.out.println(isBean(obj));
    }

    public boolean isBean (Object obj) {
        if (obj == null){
            return false;
        }
        boolean result = true;

        if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }else if (obj instanceof Integer){
            result = false;
        }

        return result;
    }
}






class User{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class ResultCheckVo extends ResultCheck{
    private String a;
    private String bb;
    private User user;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}


















