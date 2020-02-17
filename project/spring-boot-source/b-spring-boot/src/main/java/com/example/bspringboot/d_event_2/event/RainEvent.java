package com.example.bspringboot.d_event_2.event;

public class RainEvent implements WeatherEvent {
    @Override
    public String getWeather() {
        return "雨天";
    }
}
