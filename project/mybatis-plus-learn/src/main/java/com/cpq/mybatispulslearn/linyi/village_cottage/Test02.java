package com.cpq.mybatispulslearn.linyi.village_cottage;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-07
 */

public class Test02 {

    @Test
    public void test001(){
        System.out.println("*******"+StringUtils.substring("abcdef", -1));
        System.out.println("*******"+StringUtils.substring("裙楼110-1", 3, 5));
        System.out.println("*******"+StringUtils.substring("裙楼110", 3, 5));
    }



}
