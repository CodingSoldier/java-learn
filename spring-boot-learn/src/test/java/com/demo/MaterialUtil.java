package com.demo;

import com.demo.common.MyException;
import com.demo.common.Result;

import java.util.HashMap;

public class MaterialUtil {

    public static void main(String[] args) {
        HashMap map = new HashMap<>();
        map.put(new Result<>(1, "sfasf", "sdfad"), new MyException("fsadfa"));
        map.put(new Result<>(2, "22", "sdf22ad"), new MyException("fsa22dfa"));
        System.out.println(map);
    }


}
