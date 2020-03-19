package com.example.bspringboot.d_event.listener;

import com.example.bspringboot.d_event.event.SnowEvent;
import com.example.bspringboot.d_event.event.WeatherEvent;

public class SnowListener implements WeatherListener<WeatherEvent> {
    @Override
    public void onWeatherEvent(WeatherEvent event) {
        if (event instanceof SnowEvent){
            System.out.println("  监听到 "+event.getWeather());
        }
    }
}
