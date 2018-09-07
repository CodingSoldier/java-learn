package com.demo;

import com.demo.paramsvalidate.ValidateUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class T1 {

    @Test
    public void test3() {
        Map<String, Object> map = new HashMap<>();
        boolean b = ValidateUtils.isRequestTrue(new HashMap<>());
        System.out.println(b);
    }

}

