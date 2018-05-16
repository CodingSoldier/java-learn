package com.demo.validate;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class ValidateUtils<T> extends org.springframework.util.StringUtils{

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

}
