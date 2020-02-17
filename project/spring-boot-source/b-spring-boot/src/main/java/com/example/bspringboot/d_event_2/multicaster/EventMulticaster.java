package com.example.bspringboot.d_event_2.multicaster;

import com.example.bspringboot.d_event_2.event.WeatherEvent;
import com.example.bspringboot.d_event_2.listener.WeatherListener;

public interface EventMulticaster {

    void multicastEvent(WeatherEvent event);

    void addListener(WeatherListener weatherListener);

    void removeListener(WeatherListener weatherListener);

}
