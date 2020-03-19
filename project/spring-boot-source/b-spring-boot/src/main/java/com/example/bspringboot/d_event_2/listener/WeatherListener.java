package com.example.bspringboot.d_event_2.listener;

import com.example.bspringboot.d_event_2.event.WeatherEvent;

public interface WeatherListener<E extends WeatherEvent> {
    void onWeatherEvent(E event);
}
