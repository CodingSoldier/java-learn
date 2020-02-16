package com.example.bspringboot.d_event.listener;

import com.example.bspringboot.d_event.event.RainEvent;
import com.example.bspringboot.d_event.event.WeatherEvent;

public class RainListener implements WeatherListener {
    @Override
    public void onWeatherEvent(WeatherEvent event) {
        if (event instanceof RainEvent){
            System.out.println("监听到 "+event.getWeather());
        }
    }
}
