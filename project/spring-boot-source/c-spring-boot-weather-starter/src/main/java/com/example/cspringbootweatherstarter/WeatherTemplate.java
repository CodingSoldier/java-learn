package com.example.cspringbootweatherstarter;

public class WeatherTemplate {

    private WeatherProperties weatherSource;

    public WeatherTemplate(WeatherProperties weatherSource) {
        this.weatherSource = weatherSource;
    }

    public String getType(){
        return this.weatherSource.getType();
    }

    public String getRate(){
        return this.weatherSource.getRate();
    }

}
