package com.demo;

import com.demo.paramsvalidate.ValidateUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class T1 {

    @Test
    public void test3() {
        Map<String, Object> map = new HashMap<>();
        Collection c = new ArrayList();
        c.add("");
        boolean b = ValidateUtils.isNullEmptyCollection(c);
        System.out.println(b);
    }

}

