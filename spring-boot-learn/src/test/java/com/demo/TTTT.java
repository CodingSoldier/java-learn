package com.demo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TTTT {
    @Test
    public void test1(){
        List<Object> l = new ArrayList<>();
        l.add("");
        l.add(null);
        l.add(1);
        System.out.println(l.contains(""));
        System.out.println(l.contains(null));
        System.out.println(l.contains("adf"));
    }
}
