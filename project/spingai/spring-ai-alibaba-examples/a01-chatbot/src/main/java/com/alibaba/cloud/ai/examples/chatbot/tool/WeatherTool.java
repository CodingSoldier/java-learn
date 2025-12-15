package com.alibaba.cloud.ai.examples.chatbot.tool;

import org.springframework.ai.chat.model.ToolContext;

import java.util.function.BiFunction;

public class WeatherTool implements BiFunction<String, ToolContext, String> {
    @Override
    public String apply(String city, ToolContext toolContext) {
        return "It's always sunny in " + city + "!";
    }
}
