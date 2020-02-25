package com.example.bspringboot.l_starter;

import com.example.cspringbootweatherstarter.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author chenpiqian
 * @date: 2020-02-25
 */
@SpringBootTest
public class WeatherStarterTest {

    @Autowired
    WeatherService weatherService;

    @Test
    void contextLoads() {
        System.out.println("########## "+ weatherService.getRate());
        System.out.println("########## "+ weatherService.getType());

        System.out.println("########## 未启动自定义starter");
    }
}
