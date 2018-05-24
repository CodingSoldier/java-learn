package com.commonsjar;

import com.utils.BaseTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * lang3要求JDK为1.5以上
 * http://blog.csdn.net/xuxiaoxie/article/details/52095930
 * CommonLang3中的StringUtils最全解析
 */
public class Lang3 extends BaseTest {
    @Test
    public void t(){
        System.out.println("StringUtils.isEmpty()");  //false
        System.out.println(StringUtils.isEmpty(" "));  //false

        //任意一个参数为空，true。有一个参数不为空，false
        System.out.println("StringUtils.isAnyEmpty()");  //false
        System.out.println(StringUtils.isAnyEmpty(null, "p"));  //true
        System.out.println(StringUtils.isAnyEmpty("", "p"));  //true
        System.out.println(StringUtils.isAnyEmpty(" ", "p"));  //false

        System.out.println("StringUtils.isNoneEmpty()");
        System.out.println(StringUtils.isNoneEmpty(null, "p"));  //false
        System.out.println(StringUtils.isNoneEmpty(" ", "p"));   //true

        System.out.println("StringUtils.isBlank()");
        System.out.println(StringUtils.isBlank(null));  //true
        System.out.println(StringUtils.isBlank(""));   //true
        System.out.println(StringUtils.isBlank(" "));   //true，这是于isEmpty的区别
        System.out.println(StringUtils.isBlank(" a "));   //false

        System.out.println("StringUtils.strip()");
        System.out.println(StringUtils.strip("   abcnfha", "ahfc"));
        System.out.println(StringUtils.strip("  abcyx", "xyz"));

        System.out.println("StringUtils.equals()");
        System.out.println(StringUtils.equals("", null)); //false

        System.out.println("StringUtils.equalsIgnoreCase()");
        System.out.println(StringUtils.equalsIgnoreCase("abc", "ABC")); //true
        System.out.println(StringUtils.equalsIgnoreCase("", null)); //false

        System.out.println( StringUtils.containsAny("zzabyycdxx",'z','p'));

        System.out.println( StringUtils.substring("0123456789",2,7));
        System.out.println( StringUtils.substring("0123456789",-1));

        System.out.println( StringUtils.split("0123456789","a").toString());



    }
}
