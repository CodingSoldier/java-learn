package com.demo;

import com.alibaba.fastjson.JSON;
import com.demo.validate.Util;
import org.junit.Test;

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
}



























