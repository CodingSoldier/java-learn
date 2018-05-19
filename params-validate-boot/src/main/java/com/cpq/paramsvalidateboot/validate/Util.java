package com.cpq.paramsvalidateboot.validate;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Util<T> extends org.springframework.util.StringUtils{

    //空、空格
    public static boolean isBlank(String str1) {
        return isEmpty(str1) || "".equals(str1.trim());
    }

    //非空、非空格
    public static boolean isNotBlank(String str1) {
        return !isBlank(str1);
    }

    //空字符、空对象
    public static boolean isBlankStrObj(Object obj) {
        return obj == null || isBlank(objToStr(obj));
    }

    //非空字符&&非空对象
    public static boolean isNotBlankStrObj(Object obj) {
        return !isBlankStrObj(obj);
    }

    //字符相等
    public static boolean strEquals(String str1, String str2) {
        return str1 != null && str2 != null && str1.equals(str2);
    }

    //字符相等忽略大小写
    public static boolean strEqualsIgnoreCase(String str1, String str2) {
        return str1 != null && str2 != null && str1.equalsIgnoreCase(str2);
    }

    //对象转字符串
    public static String objToStr(Object object){
        return object == null ? "" : String.valueOf(object);
    }

    //删除字符串两端指定字符
    private static String trimBeginEndCharBase(String args, char beTrim, boolean b, boolean e) {
        if (Util.isEmpty(args) || Util.isEmpty(beTrim)){
            return "";
        }
        int st = 0;
        int len = args.length();
        char[] val = args.toCharArray();
        char sbeTrim = beTrim;
        if (b){
            while ((st < len) && (val[st] <= sbeTrim)) {
                st++;
            }
        }
        if (e){
            while ((st < len) && (val[len - 1] <= sbeTrim)) {
                len--;
            }
        }
        return ((st > 0) || (len < args.length())) ? args.substring(st, len) : args;
    }

    //删除字符串两端指定字符
    public static String trimBeginEndChar(String args, char beTrim) {
        return trimBeginEndCharBase(args, beTrim, true, true);
    }
    //删除字符串开头指定字符
    public static String trimBeginChar(String args, char beTrim) {
        return trimBeginEndCharBase(args, beTrim, true, false);
    }

    //是否非bean，list，map
    public static boolean isSingleType (Object obj) {
        return obj == null || obj instanceof Number
                || obj instanceof CharSequence || obj instanceof Character
                || obj instanceof Date;
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

    //校验规则，是否必填
    public static boolean isRequest(Map<String, Object> rule){
        return Boolean.parseBoolean(Util.objToStr(rule.get(ValidateMain.REQUEST)));
    }

    //读取json文件到Map<String, Object>
    public static Map<String, Object> readFileToMap(String filePath) throws IOException{
        Map<String, Object> json = new HashMap<String, Object>();
        if (isEmpty(filePath)){
            return json;
        }

        try (InputStream is = Util.class.getClassLoader().getResourceAsStream(filePath)){
            if (is != null){
                ObjectMapper mapper = new ObjectMapper();
                json = mapper.readValue(is, Map.class);
            }
        }
        return json;
    }

}
