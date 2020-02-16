package com.example.bspringboot.d_event.listener;

import com.example.bspringboot.d_event.event.WeatherEvent;

public interface WeatherListener {
    void onWeatherEvent(WeatherEvent event);
}
