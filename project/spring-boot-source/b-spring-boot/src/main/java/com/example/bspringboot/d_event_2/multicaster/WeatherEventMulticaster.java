package com.example.bspringboot.d_event_2.multicaster;

import org.springframework.stereotype.Component;

@Component
public class WeatherEventMulticaster extends AbstractEventMulticaster {
    @Override
    void doStart() {
        System.out.println("#开始广播天气");
    }

    @Override
    void doEnd() {
        System.out.println("#结束广播天气");
    }
}
