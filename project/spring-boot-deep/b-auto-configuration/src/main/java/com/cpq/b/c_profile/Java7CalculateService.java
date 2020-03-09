package com.cpq.b.c_profile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-08
 */
//当profile是Java7的时候使用这个类
@Profile("Java7")
@Service
public class Java7CalculateService implements CalculateService {

    @Override
    public Integer sum(Integer... values) {
        System.out.println("Java 7 for 循环实现 ");
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

}
