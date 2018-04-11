package com.thinkinginjava;

import org.junit.Test;

import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class I_String {
    //P300Pattern表示编译后的正则对象
    @Test
    public void t(){
        Pattern p = Pattern.compile("\\+[1-9]+");
        Matcher m = p.matcher("+1234");
        System.out.println(m.matches());
        //reset方法将Matcher对象应用于新的字符串
        m.reset("+-ppp");
        System.out.println(m.matches());
    }
}




class E10_CheckForMatch {
    public static void main(String[] args) {
        String source = "Java now has regular expressions";
        String[] regEx = {"^Java", "\\Breg.*",  "n.w\\s+h(a|i)s", "s?", "s*", "s+", "s{4}", "s{1}.",  "s{0,3}"};
        System.out.println("Source string: " + source);
        for(String pattern : regEx) {
            System.out.println("Regular expression: " + pattern + "\"");
            //链式写法获得Matcher对象
            Matcher m = Pattern.compile(pattern).matcher(source);
            //Matcher的find方法可以像迭代器一样使用
            while(m.find()) {
                System.out.println("Match \"" + m.group() +  "\" at positions " + m.start() + "-" +  (m.end() - 1));
            }
        }
    }
}




//P310
class DataHolder2 {
    private int i;
    private long l;
    private float f;
    private double d;
    private String s;
    DataHolder2(String data) {
        Scanner stdin = new Scanner(data);
        stdin.useLocale(Locale.US);
        i = stdin.nextInt();
        l = stdin.nextLong();
        f = stdin.nextFloat();
        d = stdin.nextDouble();
        s = stdin.next("\\w+");
    }
    public String toString() {
        return i + " " + l + " " + f + " " + d + " " + s;
    }
}
class E20_Scanner {
    public static void main(String[] args) {
        DataHolder2 dh =  new DataHolder2("1 2 10000000000000 1.1 1e55 Howdy Hi");
        System.out.println(dh.toString());
        System.out.println(dh.getClass().getCanonicalName());
    }
}


