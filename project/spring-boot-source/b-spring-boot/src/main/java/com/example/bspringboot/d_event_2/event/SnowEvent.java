package com.example.bspringboot.d_event_2.event;


public class SnowEvent implements WeatherEvent {
    @Override
    public String getWeather() {
        return "雪天";
    }
}
