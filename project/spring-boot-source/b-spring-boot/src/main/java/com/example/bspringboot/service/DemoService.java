package com.example.bspringboot.service;

import com.example.bspringboot.bean.Demo;
import com.example.bspringboot.mapper.DemoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DemoService {

    @Autowired
    private DemoMapper demoMapper;

    public String getDemoById(Long id) {
        return Optional
                .ofNullable(demoMapper.selectByPrimaryKey(id).toString())
                .orElse(null);
    }

}
