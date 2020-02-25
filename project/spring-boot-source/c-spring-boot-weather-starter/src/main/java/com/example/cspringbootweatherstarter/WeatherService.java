package com.example.cspringbootweatherstarter;

public class WeatherService {

    private WeatherSource weatherSource;

    public WeatherService(WeatherSource weatherSource) {
        this.weatherSource = weatherSource;
    }

    public String getType(){
        return this.weatherSource.getType();
    }

    public String getRate(){
        return this.weatherSource.getRate();
    }

}
