package com.example.bspringboot.d_event_2;

import com.example.bspringboot.d_event_2.event.RainEvent;
import com.example.bspringboot.d_event_2.event.SnowEvent;
import com.example.bspringboot.d_event_2.multicaster.WeatherEventMulticaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherRunListener {

    /**
     * 新建事件推送者，配合spring自动注入，推送事件就非常简单了，推送代码
     *         weatherRunListener.rain();
     *         weatherRunListener.snow();
     */
    @Autowired
    private WeatherEventMulticaster eventMulticaster;

    public void snow() {
        eventMulticaster.multicastEvent(new SnowEvent());
    }

    public void rain() {
        eventMulticaster.multicastEvent(new RainEvent());
    }

}
