package com.example.thingdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 公共方法
 *
 * @author chenpq05
 * @since 2022-02-09
 */
@Slf4j
public class CommonUtil {

    /**
     * 结尾符号
     */
    public static final String END_CHAR = ".*[,.，。、]$";

    private CommonUtil() {
    }

    /**
     * 返回32位UUID
     *
     * @return String
     */
    public static String uuid32() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 所有字符串都不是 null或""或" "
     *
     * @param css
     * @return
     */
    public static boolean isAllNotBlank(final CharSequence... css) {
        return !StringUtils.isAllBlank(css);
    }

    /**
     * object转string 若 object == null 返回 ""
     *
     * @param obj Object
     * @return String
     */
    public static String objectToString(Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof String) {
            return (String) obj;
        } else {
            return String.valueOf(obj);
        }
    }

    /**
     * Object转Long
     *
     * @param object Object
     * @return Long
     */
    public static Long parseLong(Object object) {
        String str = objectToString(object);
        Long num = null;
        if (!StringUtils.isEmpty(str)) {
            try {
                num = Long.parseLong(str);
            } catch (Exception ex) {
                log.error("字符串转Long异常", ex);
            }
        }
        return num;
    }

    /**
     * 字符串转Integer
     *
     * @param object Object
     * @return Integer
     */
    public static Integer parseInteger(Object object) {
        String str = objectToString(object);
        Integer num = null;
        if (!StringUtils.isEmpty(str)) {
            try {
                num = Integer.parseInt(str);
            } catch (Exception ex) {
                log.error("字符串转Integer异常", ex);
            }
        }
        return num;
    }

    /**
     * 将字符串分割为List
     *
     * @param str           字符串
     * @param separatorChar 分隔符
     * @param clazz         集合元素类型
     * @param <E>
     * @return List
     */
    public static <E> List<E> split(String str, String separatorChar, Class<E> clazz) {
        str = StringUtils.trim(str);
        String[] arr = StringUtils.split(str, separatorChar);
        if (arr == null || arr.length == 0) {
            return new ArrayList<>();
        }
        List<E> result = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (clazz.isAssignableFrom(Integer.class)) {
                result.add((E) parseInteger(arr[i]));
            } else if (clazz.isAssignableFrom(Long.class)) {
                result.add((E) parseLong(arr[i]));
            } else if (clazz.isAssignableFrom(String.class)) {
                result.add((E) arr[i]);
            }
        }
        return result;
    }

    /**
     * null转空字符串串
     *
     * @param str
     * @return
     */
    public String nullToEmptyStr(String str) {
        return str == null ? "" : str;
    }


    /**
     * 字符串结尾是否符合正则表达式
     *
     * @param str   字符串
     * @param regex 正则 例如： ".*[,.，。、]$"
     * @return
     */
    public static boolean isEndWith(String str, String regex) {
        if (StringUtils.isAnyBlank(str, regex)) {
            return false;
        }
        return Pattern.matches(regex, str);
    }

    /**
     * 字符串不相等，实现方式 !StringUtils.equals(str1, str2)
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean strNotEquals(String str1, String str2) {
        return !StringUtils.equals(str1, str2);
    }

}



