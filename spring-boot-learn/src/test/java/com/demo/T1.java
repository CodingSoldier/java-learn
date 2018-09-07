package com.demo;

import org.junit.Test;

import java.util.regex.Pattern;

public class T1 {

    @Test
    public void test3() {
        System.out.println(Pattern.matches("STRICT|LOOSE", "strict"));
    }

}

