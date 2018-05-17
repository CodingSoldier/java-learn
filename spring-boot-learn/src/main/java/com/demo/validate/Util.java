package com.demo.validate;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Util<T> extends org.springframework.util.StringUtils{

    //非空
    public static boolean isNotEmpty(String str1) {
        return !isEmpty(str1);
    }

    //空、空格
    public static boolean isBlank(String str1) {
        return isEmpty(str1) || "".equals(str1.trim());
    }

    //非空、非空格
    public static boolean isNotBlank(String str1) {
        return !(isEmpty(str1) || "".equals(str1.trim()));
    }

    //字符相等
    public static boolean strEquals(String str1, String str2) {
        return str1 != null && str2 != null && str1.equals(str2);
    }

    //字符相等忽略大小写
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
        basePath = trimBeginEndChar(basePath, '/') + "/";
        file = trimBeginChar(file, '/');
        return basePath + file;
    }

    ////数组元素添加到Set中
    //public static <T> Set<T> putArrElemToSet(Set<T> set, T[] arr) {
    //    if (arr != null && set != null){
    //        for (T e:arr){
    //            set.add(e);
    //        }
    //    }
    //    return set;
    //}

    ////获取set中的一个元素，适用于只有一个元素的set
    //public static <T> T getElemOne(Set<T> set) {
    //    T result = null;
    //    if (set != null){
    //        for (T elem:set){
    //            result = elem;
    //            break;
    //        }
    //    }
    //    return result;
    //}

    //是否为bean
    //public boolean isBean (Object obj) {
    //    if (obj == null){
    //        return false;
    //    }
    //    boolean result = true;
    //
    //    if (obj instanceof )
    //
    //    return result;
    //}

    //读取json文件到JSONObject
    public static Map<String, Object> readFileToMap(String filePath){
        Map<String, Object> json = new HashMap<String, Object>();
        if (isEmpty(filePath)){
            return json;
        }
        try (InputStream is = Util.class.getClassLoader().getResourceAsStream(filePath)){
            if (is != null){
                json = JSON.parseObject(is, Map.class);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }

}
