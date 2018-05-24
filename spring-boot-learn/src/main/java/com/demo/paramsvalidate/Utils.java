package com.demo.paramsvalidate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class Utils<T> extends org.springframework.util.StringUtils{

    //空、空格
    public static boolean isBlank(String str1) {
        return isEmpty(str1) || "".equals(str1.trim());
    }

    //非空、非空格
    public static boolean isNotBlank(String str1) {
        return !isBlank(str1);
    }

    //空字符、空对象
    public static boolean isBlankObj(Object obj) {
        return obj == null || isBlank(objToStr(obj));
    }

    //非空、非""
    public static boolean isNotBlankObj(Object obj) {
        return !isBlankObj(obj);
    }

    //字符相等
    public static boolean strEquals(String str1, String str2) {
        return str1 != null && str2 != null && str1.equals(str2);
    }

    //字符非blank且相等
    public static boolean strNotBlankEquals(String str1, String str2) {
        return str1 != null && !"".equals(str1) && str2 != null && !"".equals(str2) && str1.equals(str2);
    }

    //字符相等忽略大小写
    public static boolean strEqualsIgnoreCase(String str1, String str2) {
        return str1 != null && str2 != null && str1.equalsIgnoreCase(str2);
    }

    //对象转字符串
    public static String objToStr(Object object){
        String r = "";
        if (object == null){
            r = "";
        }else if (object instanceof Number){
            r = new BigDecimal(String.valueOf(object)).toPlainString();
        }else{
            r = String.valueOf(object);
        }
        return r;
    }

    //删除字符串两端指定字符
    private static String trimBeginEndCharBase(String args, char beTrim, boolean b, boolean e) {
        if (Utils.isEmpty(args) || Utils.isEmpty(beTrim)){
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

    //校验规则，是否必填
    public static boolean isRequest(Map<String, Object> rule){
        return Boolean.parseBoolean(Utils.objToStr(rule.get(ValidateMain.REQUEST)));
    }

    //字符串转数字，数字转double
    public static double getDouble(Object value){
        return value instanceof String ? Double.parseDouble(objToStr(value)) : ((Number)value).doubleValue();
    }

    //字符串转数字，数字转double
    public static BigDecimal getBigDecimal(Object value){
        return new BigDecimal(getDouble(value));
    }


}
