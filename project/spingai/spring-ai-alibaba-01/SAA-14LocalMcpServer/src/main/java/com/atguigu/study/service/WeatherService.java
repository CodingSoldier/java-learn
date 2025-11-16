package com.atguigu.study.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherService {

    @Tool(description = "根据城市名称获取天气预报")
    public String getWeatherForecast(String city) {
        Map<String, String> map = Map.of(
                "北京", "天气晴转多云，温度 18℃",
                "上海", "天气阴转雷阵雨，温度 17℃",
                "广州", "天气多云转阴，温度 19℃",
                "深圳", "天气雷阵雨转小雨，温度 16℃"
        );
        return map.getOrDefault(city, "没有找到该城市的天气信息");
    }

}
