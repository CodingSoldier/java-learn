package com.example.bspringboot.d_event.event;

public class SnowEvent implements WeatherEvent {
    @Override
    public String getWeather() {
        return "雪天";
    }
}
