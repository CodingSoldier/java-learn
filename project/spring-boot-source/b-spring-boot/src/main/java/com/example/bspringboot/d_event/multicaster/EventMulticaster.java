package com.example.bspringboot.d_event.multicaster;

import com.example.bspringboot.d_event.event.WeatherEvent;
import com.example.bspringboot.d_event.listener.WeatherListener;

public interface EventMulticaster {

    void multicastEvent(WeatherEvent event);

    void addListener(WeatherListener weatherListener);

    void removeListener(WeatherListener weatherListener);

}
