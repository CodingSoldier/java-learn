package com.example.bspringboot.d_event.multicaster;

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
