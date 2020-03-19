package com.example.bspringboot.d_event_2.listener;

import com.example.bspringboot.d_event_2.event.SnowEvent;
import com.example.bspringboot.d_event_2.event.WeatherEvent;
import org.springframework.stereotype.Component;

@Component
public class SnowListener implements WeatherListener<WeatherEvent> {
    @Override
    public void onWeatherEvent(WeatherEvent event) {
        if (event instanceof SnowEvent){
            System.out.println("    监听到 "+event.getWeather());
        }
    }
}
