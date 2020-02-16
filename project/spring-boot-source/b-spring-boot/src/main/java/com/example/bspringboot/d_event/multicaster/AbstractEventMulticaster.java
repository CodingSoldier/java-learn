package com.example.bspringboot.d_event.multicaster;

import com.example.bspringboot.d_event.event.WeatherEvent;
import com.example.bspringboot.d_event.listener.WeatherListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEventMulticaster implements EventMulticaster {

    private List<WeatherListener> listenerList = new ArrayList<>();

    @Override
    public void multicastEvent(WeatherEvent event) {
        /**
         * 模板设计模式：加上doStart()、doEnd()这两个虚拟方法
         */
        doStart();
        listenerList.forEach(i -> i.onWeatherEvent(event));
        doEnd();
    }

    @Override
    public void addListener(WeatherListener weatherListener) {
        listenerList.add(weatherListener);
    }

    @Override
    public void removeListener(WeatherListener weatherListener) {
        listenerList.remove(weatherListener);
    }

    abstract void doStart();

    abstract void doEnd();
}
