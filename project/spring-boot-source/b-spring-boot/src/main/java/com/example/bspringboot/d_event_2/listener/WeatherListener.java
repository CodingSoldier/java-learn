package com.example.bspringboot.d_event_2.listener;

import com.example.bspringboot.d_event_2.event.WeatherEvent;

public interface WeatherListener {
    void onWeatherEvent(WeatherEvent event);
}
