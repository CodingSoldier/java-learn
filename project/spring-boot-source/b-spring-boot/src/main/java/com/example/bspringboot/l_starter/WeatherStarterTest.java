package com.example.bspringboot.l_starter;

import com.example.weatherspringbootstarter.WeatherTemplate;
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
    WeatherTemplate weatherTemplate;

    @Test
    void contextLoads() {
        weatherTemplate.weatherPrint();
    }
}
