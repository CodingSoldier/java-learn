package com.example.bspringboot.d_event.listener;

import com.example.bspringboot.d_event.event.WeatherEvent;

public interface WeatherListener<E extends WeatherEvent> {
    void onWeatherEvent(E event);
}
