package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.PvMsg;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * author chenpiqian 2018-05-25
 */
public class ValidateUtils<T> extends org.springframework.util.StringUtils{

    private static final Logger LOGGER = Logger.getLogger("@ParamsValidate");

    public static void logWarning(String msg){
        LOGGER.log(Level.WARNING, msg);
    }
    public static void logWarning(String msg, Method method, Throwable e){
        log(Level.WARNING, msg, method, e);
    }

    public static void logSevere(String msg, Method method, Throwable e){
        log(Level.SEVERE, msg, method, e);
    }

    public static void log(Level level, String msg, Method method, Throwable e){
        if (method != null){
            msg = String.format("Error Method: %s.%s%nException Message: %s",method.getDeclaringClass().getName(),method.getName(),msg);
        }else {
            msg = String.format("Exception Message: %s",msg);
        }
        LOGGER.log(level, msg, e);
    }

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
        if (isEmpty(args) || isEmpty(beTrim)){
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

    //字符串转数字，数字转double
    public static double getDouble(Object value){
        return value instanceof String ? Double.parseDouble(objToStr(value)) : ((Number)value).doubleValue();
    }

    //字符串转数字，数字转double
    public static BigDecimal getBigDecimal(Object value){
        return BigDecimal.valueOf(getDouble(value));
    }

    //校验规则，request是否为true
    public static boolean isRequestTrue(Map<String, Object> rules){
        return Boolean.parseBoolean(objToStr(rules.get(PvMsg.REQUEST)));
    }

    //校验规则，request是否为false
    public static boolean isRequestFalse(Map<String, Object> rules){
        return "false".equals(objToStr(rules.get(PvMsg.REQUEST)).toLowerCase());
    }

    //是否为null、""、空集合
    public static boolean isEmptySize0(Object obj) {
        boolean r = false;
        if (isBlankObj(obj)){
            r = true;
        }else if (obj instanceof Collection){
            r = ((Collection)obj).size() == 0;
        }else if (obj instanceof Map){
            r = ((Map)obj).size() == 0;
        }
        return r;
    }

    //Collection有空元素
    public static boolean collectionHasEmpty(Collection collection) {
        return collection.size()==0 || collection.contains("") || collection.contains(null);
    }

    //删除map中value为空的entry
    public static void deleteMapEmptyValue(Map<String, Object> map){
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            boolean hasEmpty = false;
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            if (isEmptySize0(value)){
                hasEmpty = true;
            }else if (value instanceof Map){
                deleteMapEmptyValue((Map<String, Object>)value);
            }else if (value instanceof Collection){
                loopList((Collection)value);
            }else {
                hasEmpty = isEmpty(objToStr(value)) ? true : false;
            }
            if (hasEmpty) {
                iterator.remove();
            }
        }
    }

    //Collection循环
    public static void loopList(Collection collection){
        Iterator it = collection.iterator();
        while (it.hasNext()){
            Object value = it.next();
            if (value instanceof Map){
                deleteMapEmptyValue((Map<String, Object>)value);
            }else if (value instanceof Collection){
                loopList((Collection)value);
            }
        }
    }

}
