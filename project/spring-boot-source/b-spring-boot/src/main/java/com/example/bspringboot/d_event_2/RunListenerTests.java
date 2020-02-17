package com.example.bspringboot.d_event_2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RunListenerTests {

    @Autowired
    WeatherRunListener weatherRunListener;

    @Test
    void contextLoads() {
        weatherRunListener.rain();
        weatherRunListener.snow();
    }

}
