package com.example.bspringboot.d_event_2.listener;


import com.example.bspringboot.d_event_2.event.RainEvent;
import com.example.bspringboot.d_event_2.event.WeatherEvent;
import org.springframework.stereotype.Component;

@Component
public class RainListener implements WeatherListener<WeatherEvent> {
    @Override
    public void onWeatherEvent(WeatherEvent event) {
        if (event instanceof RainEvent){
            System.out.println("    监听到 "+event.getWeather());
        }
    }
}
