package com.example.bspringboot.d_event_2.multicaster;


import com.example.bspringboot.d_event_2.event.WeatherEvent;
import com.example.bspringboot.d_event_2.listener.WeatherListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractEventMulticaster implements EventMulticaster {

    /**
     * 可以自动注入WeatherListener所有实现类到集合属性中
     */
    @Autowired
    private List<WeatherListener> listenerList;

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
