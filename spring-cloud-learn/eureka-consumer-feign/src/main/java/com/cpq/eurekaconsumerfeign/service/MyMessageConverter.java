package com.cpq.eurekaconsumerfeign.service;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjq on 2018/6/21.
 */
public class MyMessageConverter extends MappingJackson2HttpMessageConverter {

    public MyMessageConverter(){
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypes);
    }
}
