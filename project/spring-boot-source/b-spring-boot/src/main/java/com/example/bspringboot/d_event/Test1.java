package com.example.bspringboot.d_event;

import com.example.bspringboot.d_event.event.RainEvent;
import com.example.bspringboot.d_event.event.SnowEvent;
import com.example.bspringboot.d_event.event.WeatherEvent;
import com.example.bspringboot.d_event.listener.RainListener;
import com.example.bspringboot.d_event.listener.SnowListener;
import com.example.bspringboot.d_event.listener.WeatherListener;
import com.example.bspringboot.d_event.multicaster.WeatherEventMulticaster;

public class Test1 {

    /**
     * 监听器模式
     */
    public static void main(String[] args) {

        WeatherEvent rainEvent = new RainEvent();
        WeatherEvent snowEvent = new SnowEvent();

        WeatherListener rainListener = new RainListener();
        WeatherListener snowListener = new SnowListener();

        WeatherEventMulticaster weatherEventMulticaster = new WeatherEventMulticaster();
        weatherEventMulticaster.addListener(rainListener);
        weatherEventMulticaster.addListener(snowListener);

        weatherEventMulticaster.multicastEvent(rainEvent);
        weatherEventMulticaster.multicastEvent(snowEvent);
        // 删除监听器
        weatherEventMulticaster.removeListener(rainListener);
        weatherEventMulticaster.multicastEvent(rainEvent);
        weatherEventMulticaster.multicastEvent(snowEvent);

    }

}
