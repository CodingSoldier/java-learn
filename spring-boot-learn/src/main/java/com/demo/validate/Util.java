package com.demo.validate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

public class Util<T> extends org.springframework.util.StringUtils{

    public static Boolean notRequestResponse(Object param){
        return param instanceof HttpServletRequest || param instanceof ServletResponse;
    }

    public static Boolean isRequest(Object param){
        return param instanceof HttpServletRequest;
    }

    public static boolean isNotEmpty(String str1) {
        return !isEmpty(str1);
    }

    public static boolean strEquals(String str1, String str2) {
        return str1 != null && str2 != null && str1.equals(str2);
    }

    public static boolean strEqualsIgnoreCase(String str1, String str2) {
        return str1 != null && str2 != null && str1.equalsIgnoreCase(str2);
    }

    //删除字符串两端指定字符
    public static String trimBeginEndChar(String args, char beTrim) {
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
        while ((st < len) && (val[len - 1] <= sbeTrim)) {
            len--;
        }
        return ((st > 0) || (len < args.length())) ? args.substring(st, len) : args;
    }
    //删除字符串开头指定字符
    public static String trimBeginChar(String args, char beTrim) {
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
        return ((st > 0) || (len < args.length())) ? args.substring(st, len) : args;
    }

    //返回文件路径
    public static String makeFilePath(String basePath, String file) {
        basePath = trimBeginEndChar(basePath, '/');
        file = trimBeginChar(file, '/');
        return basePath + file;
    }

    //获取set中的一个元素，适用于只有一个元素的set
    public static <T> T getElemOne(Set<T> set) {
        T result = null;
        if (set != null){
            for (T elem:set){
                result = elem;
                break;
            }
        }
        return result;
    }

    //读取json文件到JSONObject
    public static JSONObject readFileToJSONObject(String filePath){
        if (isEmpty(filePath)){
            return null;
        }

        JSONObject jsonObject = null;
        StringBuffer sb = new StringBuffer();
        try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(
                                Util.class.getClassLoader().getResourceAsStream(filePath)))){

            String line = "";
            while ((line = br.readLine()) != null){
                sb.append(line);
            }

        }catch (IOException e){

        }
        return JSON.parseObject(sb.toString());
    }

}
