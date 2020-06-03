package com.example.weatherspringbootstarter;

public class WeatherTemplate {

    private WeatherProperties weatherProperties;

    public WeatherTemplate(WeatherProperties weatherProperties) {
        this.weatherProperties = weatherProperties;
    }

    public void weatherPrint(){
        System.out.println(String.format("执行自定义功能，type是：%s。rate是：%s", weatherProperties.getType(), weatherProperties.getRate()));
    }

}
